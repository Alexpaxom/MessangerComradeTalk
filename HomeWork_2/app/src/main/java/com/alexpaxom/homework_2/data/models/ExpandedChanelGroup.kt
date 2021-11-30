package com.alexpaxom.homework_2.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExpandedChanelGroup(
    override val typeId: Int = 0,
    val channel: ChannelItem,
    val topics: List<TopicItem> = listOf(),
): Parcelable, ListItem {
    fun toExpandableItemList(needExpanded:Boolean) : List<ExpandedChannelItem> {
        return if(needExpanded)
            listOf(channel, *topics.toTypedArray())
        else
            listOf(channel)
    }

}