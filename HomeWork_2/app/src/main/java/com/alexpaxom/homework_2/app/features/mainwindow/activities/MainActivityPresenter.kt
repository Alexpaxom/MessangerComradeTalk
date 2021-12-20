package com.alexpaxom.homework_2.app.features.mainwindow.activities

import com.alexpaxom.homework_2.app.features.baseelements.BaseView
import com.alexpaxom.homework_2.data.models.UserItem
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.UserProfileUseCaseZulip
import com.alexpaxom.homework_2.di.screen.ScreenScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@ScreenScope
@InjectViewState
class MainActivityPresenter @Inject constructor(
    private val profileHandler: UserProfileUseCaseZulip
): MvpPresenter<BaseView<MainActivityState, MainActivityEffect>>() {

    var ownUser: UserItem? = null

    private var currentViewState: MainActivityState = MainActivityState()
        set(value) {
            field = value
            viewState.processState(value)
        }

    private val compositeDisposable = CompositeDisposable()

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