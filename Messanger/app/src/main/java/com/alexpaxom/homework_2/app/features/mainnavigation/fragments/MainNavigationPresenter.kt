package com.alexpaxom.homework_2.app.features.mainnavigation.fragments

import com.alexpaxom.homework_2.app.features.baseelements.BaseView
import com.alexpaxom.homework_2.app.features.mainnavigation.activities.MainActivityEvent
import com.alexpaxom.homework_2.app.features.mainnavigation.activities.MainNavigationEffect
import com.alexpaxom.homework_2.app.features.mainnavigation.activities.MainNavigationState
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
class MainNavigationPresenter @Inject constructor(
    private val profileHandler: UserProfileUseCaseZulip
): MvpPresenter<BaseView<MainNavigationState, MainNavigationEffect>>() {

    var ownUser: UserItem? = null

    private var currentViewState: MainNavigationState = MainNavigationState()
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
                currentViewState = MainNavigationState(isEmptyLoad = true)
            }
            .subscribeBy(
                onNext = {
                    ownUser = it.data

                },
                onError = {
                    ownUser?.let{
                        currentViewState = MainNavigationState( userInfo = ownUser )
                    }
                    viewState.processEffect(
                        MainNavigationEffect.ShowError(
                            it.localizedMessage ?: "Error when get current user param"
                        )
                    )
                },
                onComplete = {
                    currentViewState = MainNavigationState( userInfo = ownUser )
                }
            )
            .addTo(compositeDisposable)
    }



}