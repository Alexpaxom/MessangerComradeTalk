package com.alexpaxom.homework_2.app.fragments

import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.UserProfileUseCaseZulip
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.UserStatusUseCaseZulip
import com.alexpaxom.homework_2.di.screen.ScreenScope
import com.alexpaxom.homework_2.domain.cache.helpers.CachedWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import retrofit2.HttpException
import java.util.*
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

@ScreenScope
@InjectViewState
class ProfilePresenter @Inject constructor(
    private val searchUsers: UserProfileUseCaseZulip,
    private val userStatusInfo: UserStatusUseCaseZulip,
) : MvpPresenter<BaseView<ProfileViewState, ProfileEffect>>() {

    private var currentViewState: ProfileViewState = ProfileViewState()
        set(value) {
            field = value
            viewState.processState(value)
        }

    private val compositeDisposable = CompositeDisposable()

    fun processEvent(event: ProfileEvent) {
        when(event) {
            is ProfileEvent.LoadUserInfo -> loadUserData(event.userId)
        }
    }

    private fun loadUserData(userId: Int) {

        searchUsers.getUserByID(userId)
            .flatMap { userWrap ->
                return@flatMap when(userWrap) {
                    is CachedWrapper.CachedData ->
                        Observable.just(userWrap)

                    is CachedWrapper.OriginalData ->
                        Observable.just(userWrap)
                            .zipWith(userStatusInfo.getStatusForUser(userId).toObservable()) { user, status ->
                                CachedWrapper.OriginalData(user.data.copy(status = status))
                    }
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread(), true)
            .doOnSubscribe {
                currentViewState = ProfileViewState(isEmptyLoading = true)
            }
            .subscribeBy(
                onNext = {
                    currentViewState = ProfileViewState(user = it.data)
                },
                onError = { processError(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun processError(error: Throwable) {
        val errorMsg = if(error is HttpException)
            error.response()?.errorBody()?.string() ?: "Http Error!"
        else
            error.localizedMessage ?: "Error!"

        viewState.processEffect(
            ProfileEffect.ShowError(errorMsg)
        )
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}