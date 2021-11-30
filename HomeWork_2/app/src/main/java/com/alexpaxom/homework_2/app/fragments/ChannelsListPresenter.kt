package com.alexpaxom.homework_2.app.fragments

import android.util.Log
import com.alexpaxom.homework_2.data.models.ChannelItem
import com.alexpaxom.homework_2.data.models.ExpandedChanelGroup
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.SearchExpandedChannelGroupZulip
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

    private var searchChannelsSubject: BehaviorSubject<String>? = null
    private val searchExpandedChannelGroup = SearchExpandedChannelGroupZulip()

    private val expandedChannelsIds = HashSet<Int>()

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
        // Сохраняем состояние раскрытости списка в локальном сете
        if(expandedChannelsIds.contains(channel.id) && !channel.isExpanded)
            expandedChannelsIds.remove(channel.id)
        else
            expandedChannelsIds.add(channel.id)


        currentViewState = currentViewState.copy(
            channels = refreshExpandedState(currentViewState.channels)
        )
    }

    private fun initChannelsGroupSearchListener() {
        searchChannelsSubject = BehaviorSubject.create()
        searchChannelsSubject!!
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                currentViewState = ChannelsViewState(isEmptyLoad = true)
            }
            .observeOn(Schedulers.io())
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .switchMap {
                if(subscribedFilterFlag)
                    searchExpandedChannelGroup.searchInSubscribedChannelGroups(it)
                        .subscribeOn(Schedulers.io())
                else
                    searchExpandedChannelGroup.searchInAllChannelGroups(it)
                        .subscribeOn(Schedulers.io())
            }
            .doOnError { initChannelsGroupSearchListener() }
            .observeOn(AndroidSchedulers.mainThread(), true)
            .subscribeBy(
                onNext = { channelsGroups ->

                    currentViewState = ChannelsViewState(
                        channels = refreshExpandedState(channelsGroups.data)
                    )
                },
                onError = { processError(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun refreshExpandedState(
        channelsGroup: List<ExpandedChanelGroup>
    ) : List<ExpandedChanelGroup> {

        return channelsGroup.map { channelGroup ->
                channelGroup.copy(
                    channel = channelGroup.channel.copy(
                        isExpanded = expandedChannelsIds.contains(channelGroup.channel.id)
                    )
                )
        }
    }

    private fun searchChannels(searchString: String) {
        searchChannelsSubject?.onNext(searchString)
    }

    private fun processError(error: Throwable) {
        val errorMsg = if(error is HttpException)
            error.response()?.errorBody()?.string() ?: "Http Error!"
        else
            error.localizedMessage ?: "Error!"

        viewState.processEffect(
            ChannelsListEffect.ShowError(errorMsg)
        )
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}