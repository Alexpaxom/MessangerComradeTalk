package com.alexpaxom.homework_2.app.activities

import com.alexpaxom.homework_2.app.fragments.BaseView
import com.alexpaxom.homework_2.data.models.UserItem
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.UserProfileUseCaseZulip
import com.alexpaxom.homework_2.di.screen.ScreenComponent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import moxy.MvpPresenter
import javax.inject.Inject

class MainActivityPresenter(
    screenComponent: ScreenComponent
): MvpPresenter<BaseView<MainActivityState, MainActivityEffect>>() {

    @Inject
    lateinit var profileHandler: UserProfileUseCaseZulip

    var ownUser: UserItem? = null

    private var currentViewState: MainActivityState = MainActivityState()
        set(value) {
            field = value
            viewState.processState(value)
        }

    private val compositeDisposable = CompositeDisposable()

    init {
        screenComponent.inject(this)
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