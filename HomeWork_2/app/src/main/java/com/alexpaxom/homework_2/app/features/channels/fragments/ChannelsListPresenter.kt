package com.alexpaxom.homework_2.app.features.channels.fragments

import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.features.baseelements.BaseView
import com.alexpaxom.homework_2.app.features.channels.helpers.ChannelsHandler
import com.alexpaxom.homework_2.data.models.ChannelItem
import com.alexpaxom.homework_2.data.models.ExpandedChanelGroup
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.EditChannelsUseCaseZulip
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.EditTopicsUseCaseZulip
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.LoadChannelUseCaseZulip
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import moxy.MvpPresenter
import retrofit2.HttpException

abstract class ChannelsListPresenter(
    private val loadChannelUseCase: LoadChannelUseCaseZulip,
    private val editChannelsUseCaseZulip: EditChannelsUseCaseZulip,
    private val editTopicsUseCaseZulip: EditTopicsUseCaseZulip
): MvpPresenter<BaseView<ChannelsViewState, ChannelsListEffect>>() {
    protected open var currentViewState: ChannelsViewState = ChannelsViewState()
        set(value) {
            field = value
            viewState.processState(value)
        }

    protected open var searchChannelsSubject: BehaviorSubject<String>? = null

    protected val expandedChannelsIds = HashSet<Int>()

    private val compositeDisposable = CompositeDisposable()
    private val channelsHandler = ChannelsHandler()

    fun processTopicContextMenuSelect(event: ChannelsListEvent.TopicContextMenuSelect) {
        when(event.menuItemId) {
            R.id.delete_topic_menu_item -> { removeTopic(event) }
        }
    }

    fun processChannelContextMenuSelect(event: ChannelsListEvent.ChannelContextMenuSelect) {
        when(event.menuItemId) {
            R.id.archive_channel_menu_item -> { archiveChannel(event) }
        }
    }

    private fun removeTopic(event: ChannelsListEvent.TopicContextMenuSelect) {
        editTopicsUseCaseZulip.removeTopic(event.topic.channelId, event.topic.name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    currentViewState = currentViewState.copy(
                        channels = channelsHandler.deleteTopic(event.topic, currentViewState.channels)
                    )
                },
                onError = {
                    processError(it)
                }
            )
            .addTo(compositeDisposable)
    }

    private fun archiveChannel(event: ChannelsListEvent.ChannelContextMenuSelect) {
        editChannelsUseCaseZulip.archiveChannel(event.channel.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    currentViewState = currentViewState.copy(
                        channels = channelsHandler.deleteChannel(event.channel, currentViewState.channels)
                    )
                },
                onError = {
                    processError(it)
                }
            )
            .addTo(compositeDisposable)
    }

    open fun processEvent(event: ChannelsListEvent) {
        when(event) {
            is ChannelsListEvent.SearchInChannelGroup -> searchChannels(event.searchString)
            is ChannelsListEvent.ExpandedStateChange -> changeExpandGroupState(event.channel)
            is ChannelsListEvent.ChannelContextMenuSelect -> processChannelContextMenuSelect(event)
            is ChannelsListEvent.TopicContextMenuSelect -> processTopicContextMenuSelect(event)
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