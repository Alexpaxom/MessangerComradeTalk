package com.alexpaxom.homework_2.app.features.channels.helpers

import com.alexpaxom.homework_2.data.models.ChannelItem
import com.alexpaxom.homework_2.data.models.ExpandedChanelGroup
import com.alexpaxom.homework_2.data.models.TopicItem

class ChannelsHandler {
    fun deleteTopic(
        deleteTopicItem: TopicItem,
        oldChannelGroup: List<ExpandedChanelGroup>
    ): List<ExpandedChanelGroup> {
        val arrayList = arrayListOf<ExpandedChanelGroup>()
        arrayList.addAll(oldChannelGroup)

        val itemPos = oldChannelGroup.indexOfFirst { it.channel.id == deleteTopicItem.channelId }

        if(itemPos != -1) {
            arrayList[itemPos] = arrayList[itemPos].copy(
                topics = arrayList[itemPos].topics.filter { it.name != deleteTopicItem.name  }
            )
        }

        return arrayList
    }

    fun deleteChannel(
        deleteChanelItem: ChannelItem,
        oldChannelGroup: List<ExpandedChanelGroup>
    ): List<ExpandedChanelGroup> {

        return oldChannelGroup.filter { it.channel.id != deleteChanelItem.id }
    }
}