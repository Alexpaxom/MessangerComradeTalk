package com.alexpaxom.homework_2.app.fragments

import com.alexpaxom.homework_2.data.models.ChannelItem
import com.alexpaxom.homework_2.data.models.ExpandedChanelGroup
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.SearchExpandedChannelGroupZulipImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import moxy.MvpPresenter
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

class ChannelsListPresenter(
    val subscribedFilterFlag: Boolean
): MvpPresenter<BaseView<ChannelsViewState, ChannelsListEffect>>() {
    private var currentViewState: ChannelsViewState = ChannelsViewState()
        set(value) {
            field = value
            viewState.processState(value)
        }

    private val compositeDisposable = CompositeDisposable()

    private val searchChannelsSubject: BehaviorSubject<String> = BehaviorSubject.create()
    private val searchExpandedChannelGroup = SearchExpandedChannelGroupZulipImpl()

    init {
        initChannelsGroupSearchListener()
        // загружаем первые данные о пользователях
        searchChannels(ChannelsListFragment.INITIAL_SEARCH_QUERY)
    }

    fun processEvent(event: ChannelsListEvent) {
        when(event) {
            is ChannelsListEvent.SearchInChannelGroup -> searchChannels(event.searchString)
            is ChannelsListEvent.ExpandedStateChange -> changeExpandGroupState(event.channel)
        }
    }

    private fun changeExpandGroupState(channel: ChannelItem) {
        val newChannels = arrayListOf<ExpandedChanelGroup>()
        newChannels.addAll(currentViewState.channels)
        newChannels.indexOfLast { it.channel.id == channel.id }.let {
            newChannels[it] = newChannels[it].copy(
                channel = channel
            )
        }

        currentViewState = currentViewState.copy(
            channels = newChannels
        )
    }

    private fun initChannelsGroupSearchListener() {
        searchChannelsSubject
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                currentViewState = ChannelsViewState(isEmptyLoad = true)
            }
            .observeOn(Schedulers.io())
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .switchMapSingle {
                if(subscribedFilterFlag)
                    searchExpandedChannelGroup.searchInSubscribedChannelGroups(it).subscribeOn(
                        Schedulers.io())
                else
                    searchExpandedChannelGroup.searchInAllChannelGroups(it).subscribeOn(Schedulers.io())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    currentViewState = ChannelsViewState(channels = it)
                },
                onError = {  error ->
                    val errorMsg = if(error is HttpException)
                        error.response()?.errorBody()?.string() ?: "Http Error!"
                    else
                        error.localizedMessage ?: "Error!"

                    viewState.processEffect(
                        ChannelsListEffect.ShowError(errorMsg)
                    )
                }
            )
            .addTo(compositeDisposable)
    }

    private fun searchChannels(searchString: String) {
        searchChannelsSubject.onNext(searchString)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}