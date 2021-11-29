package com.alexpaxom.homework_2.app.activities

import com.alexpaxom.homework_2.app.fragments.BaseView
import com.alexpaxom.homework_2.data.models.UserItem
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.UserProfileUseCaseZulipApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import moxy.MvpPresenter

class MainActivityPresenter: MvpPresenter<BaseView<MainActivityState, MainActivityEffect>>() {

    var ownUser: UserItem? = null

    private var currentViewState: MainActivityState = MainActivityState()
        set(value) {
            field = value
            viewState.processState(value)
        }

    private val compositeDisposable = CompositeDisposable()

    private val profileHandler = UserProfileUseCaseZulipApi()

    init {
        loadOwnUserInfo()
    }

    fun processEvent(event: MainActivityEvent) {
        when(event) {
            MainActivityEvent.GetOwnUserInfo -> loadOwnUserInfo()
        }
    }

    fun loadOwnUserInfo() {
        profileHandler.getUserByID()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread(), true)
            .doOnSubscribe {
                currentViewState = MainActivityState(isEmptyLoad = true)
            }
            .subscribeBy(
                onNext = {
                    ownUser = it.data

                },
                onError = {
                    ownUser?.let{
                        currentViewState = MainActivityState( userInfo = ownUser )
                    }
                    viewState.processEffect(MainActivityEffect.ShowError(it.localizedMessage?:"Error when get current user param"))
                },
                onComplete = {
                    currentViewState = MainActivityState( userInfo = ownUser )
                }
            )
            .addTo(compositeDisposable)
    }



}