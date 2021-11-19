package com.alexpaxom.homework_2.app.fragments

import com.alexpaxom.homework_2.data.models.ChannelItem
import com.alexpaxom.homework_2.data.models.ExpandedChanelGroup

data class ChannelsViewState (
    val channels: List<ExpandedChanelGroup> = listOf(),
    val isEmptyLoad: Boolean = false
)

sealed interface ChannelsListEvent {
    class SearchInChannelGroup(val searchString: String): ChannelsListEvent
    class ExpandedStateChange(val channel:ChannelItem): ChannelsListEvent
}

sealed interface ChannelsListEffect {
    class ShowError(val error: String): ChannelsListEffect
}