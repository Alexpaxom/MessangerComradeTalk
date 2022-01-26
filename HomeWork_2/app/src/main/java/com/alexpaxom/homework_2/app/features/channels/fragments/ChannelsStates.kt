package com.alexpaxom.homework_2.app.features.channels.fragments

import com.alexpaxom.homework_2.data.models.ChannelItem
import com.alexpaxom.homework_2.data.models.ExpandedChanelGroup
import com.alexpaxom.homework_2.data.models.TopicItem

data class ChannelsViewState (
    val channels: List<ExpandedChanelGroup> = listOf(),
    val isEmptyLoad: Boolean = false
)

sealed interface ChannelsListEvent {
    class SearchInChannelGroup(val searchString: String): ChannelsListEvent
    class ExpandedStateChange(val channel:ChannelItem): ChannelsListEvent
    class TopicContextMenuSelect(val menuItemId: Int,  val topic:TopicItem): ChannelsListEvent
    class ChannelContextMenuSelect(val menuItemId: Int,  val channel:ChannelItem): ChannelsListEvent
}

sealed interface ChannelsListEffect {
    class ShowError(val error: String): ChannelsListEffect
}