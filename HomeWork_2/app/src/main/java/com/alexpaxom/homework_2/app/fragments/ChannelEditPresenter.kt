package com.alexpaxom.homework_2.app.fragments

import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.EditChannelsUseCaseZulip
import com.alexpaxom.homework_2.di.screen.ScreenScope
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
class ChannelEditPresenter @Inject constructor(
    private val editChannelsUseCaseZulip: EditChannelsUseCaseZulip
): MvpPresenter<BaseView<ChannelEditState, ChannelEditEffect>>(){

    private var currentViewState: ChannelEditState = ChannelEditState()
        set(value) {
            field = value
            viewState.processState(value)
        }

    private val compositeDisposable = CompositeDisposable()

    fun processEvent(event: ChannelEditEvent) {
        when (event) {
            is ChannelEditEvent.CreateOrUpdateChannel ->
                createChannel(event.channelName, event.channelDescription)
        }
    }

    private fun createChannel(channelName: String, channelDescription: String) {
        editChannelsUseCaseZulip.createChannel(channelName, channelDescription)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (
                onSuccess = {
                    currentViewState = ChannelEditState(
                        isSuccess = true
                    )
                },
                onError = {
                    processError(it)
                }
            )
            .addTo(compositeDisposable)
    }

    private fun processError(error: Throwable) {
        val errorMsg = if(error is HttpException)
            error.response()?.errorBody()?.string() ?: "Http Error!"
        else
            error.localizedMessage ?: "Error!"

        viewState.processEffect(
            ChannelEditEffect.ShowError(errorMsg)
        )
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

}