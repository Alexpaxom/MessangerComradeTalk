package com.alexpaxom.homework_2.app.features.channels.fragments

import com.alexpaxom.homework_2.app.features.baseelements.BaseView
import com.alexpaxom.homework_2.data.models.ChannelItem
import com.alexpaxom.homework_2.data.models.ExpandedChanelGroup
import io.reactivex.subjects.BehaviorSubject
import moxy.MvpPresenter
import retrofit2.HttpException

open class ChannelsListPresenter: MvpPresenter<BaseView<ChannelsViewState, ChannelsListEffect>>() {
    protected open var currentViewState: ChannelsViewState = ChannelsViewState()
        set(value) {
            field = value
            viewState.processState(value)
        }

    protected open var searchChannelsSubject: BehaviorSubject<String>? = null

    protected val expandedChannelsIds = HashSet<Int>()

    open fun processEvent(event: ChannelsListEvent) {
        when(event) {
            is ChannelsListEvent.SearchInChannelGroup -> searchChannels(event.searchString)
            is ChannelsListEvent.ExpandedStateChange -> changeExpandGroupState(event.channel)
        }
    }

    protected open fun changeExpandGroupState(channel: ChannelItem) {
        // Сохраняем состояние раскрытости списка в локальном сете
        if(expandedChannelsIds.contains(channel.id) && !channel.isExpanded)
            expandedChannelsIds.remove(channel.id)
        else
            expandedChannelsIds.add(channel.id)


        currentViewState = currentViewState.copy(
            channels = refreshExpandedState(currentViewState.channels)
        )
    }

    protected open fun initChannelsGroupSearchListener() {
        searchChannelsSubject = BehaviorSubject.create()
    }

    protected open fun refreshExpandedState(
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

    protected open fun searchChannels(searchString: String) {
        searchChannelsSubject?.onNext(searchString)
    }

    protected open fun processError(error: Throwable) {
        val errorMsg = if(error is HttpException)
            error.response()?.errorBody()?.string() ?: "Http Error!"
        else
            error.localizedMessage ?: "Error!"

        viewState.processEffect(
            ChannelsListEffect.ShowError(errorMsg)
        )
    }
}