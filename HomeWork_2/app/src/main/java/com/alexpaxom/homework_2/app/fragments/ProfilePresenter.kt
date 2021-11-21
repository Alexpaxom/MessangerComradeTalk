package com.alexpaxom.homework_2.app.fragments

import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.UserProfileUseCaseZulipApi
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.UserStatusUseCaseZulipApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import retrofit2.HttpException

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
            .zipWith(userStatusInfo.getStatusForUser(userId)) { user, status ->
                user.copy(status = status)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                currentViewState = ProfileViewState(isEmptyLoading = true)
            }
            .subscribeBy(
                onSuccess = {
                    currentViewState = ProfileViewState(user = it)
                },
                onError = { error ->
                    val errorMsg = if(error is HttpException)
                        error.response()?.errorBody()?.string() ?: "Http Error!"
                    else
                        error.localizedMessage ?: "Error!"

                    viewState.processEffect(
                        ProfileEffect.ShowError(errorMsg)
                    )
                }
            )
            .addTo(compositeDisposable)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}