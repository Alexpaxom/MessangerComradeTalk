package com.alexpaxom.homework_2.app.fragments

import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.LoadTopicsUseCaseZulip
import com.alexpaxom.homework_2.di.screen.ScreenScope
import com.alexpaxom.homework_2.domain.cache.helpers.CachedWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import retrofit2.HttpException
import javax.inject.Inject

@ScreenScope
@InjectViewState
class SelectTopicPresenter @Inject constructor(
    private val loadTopicsUseCaseZulip: LoadTopicsUseCaseZulip
): MvpPresenter<BaseView<SelectTopicState, SelectTopicEffect>>() {

    private var currentViewState: SelectTopicState = SelectTopicState()
        set(value) {
            field = value
            viewState.processState(value)
        }

    private val compositeDisposable = CompositeDisposable()

    fun processEvent(event: SelectTopicEvent) {
        when(event) {
            is SelectTopicEvent.SearchTopics -> searchTopics(event)
        }
    }

    private fun searchTopics(event: SelectTopicEvent.SearchTopics) {
        loadTopicsUseCaseZulip.searchTopics(event.channelId, event.searchString)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread(), true)
            .doOnSubscribe {
                currentViewState = SelectTopicState(
                    topics = listOf(),
                    isEmptyLoading = true
                )
            }
            .subscribeBy (
                onNext = {  topicsWrapper ->
                    currentViewState = SelectTopicState(
                        topics = topicsWrapper.data,
                        isEmptyLoading = topicsWrapper is CachedWrapper.CachedData
                    )
                },
                onError = { processError(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun processError(error: Throwable) {
        val errorMsg = if(error is HttpException)
            error.response()?.errorBody()?.string() ?: "Load messages http Error!"
        else
            error.localizedMessage ?: "Load messages error!"

        viewState.processEffect(
            SelectTopicEffect.ShowError(errorMsg)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}