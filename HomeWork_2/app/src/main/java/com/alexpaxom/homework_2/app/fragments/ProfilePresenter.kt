package com.alexpaxom.homework_2.app.fragments

import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.UserProfileUseCaseZulipApi
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.UserStatusUseCaseZulipApi
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

@InjectViewState
class ProfilePresenter : MvpPresenter<BaseView<ProfileViewState, ProfileEffect>>() {

    private var currentViewState: ProfileViewState = ProfileViewState()
        set(value) {
            field = value
            viewState.processState(value)
        }

    private val searchUsers = UserProfileUseCaseZulipApi()
    private val userStatusInfo = UserStatusUseCaseZulipApi()

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
                    is CachedWrapper.CachedData,
                    is CachedWrapper.ErrorResult->
                        Observable.just(userWrap)

                    is CachedWrapper.OriginalData ->
                        Observable.just(userWrap)
                            .zipWith(userStatusInfo.getStatusForUser(userId).toObservable()) { user, status ->
                                CachedWrapper.OriginalData(user.data.copy(status = status))
                    }
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                currentViewState = ProfileViewState(isEmptyLoading = true)
            }
            .subscribeBy(
                onNext = {
                    if(it !is CachedWrapper.ErrorResult)
                        currentViewState = ProfileViewState(user = it.data)
                    else
                        processError(it.error)
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