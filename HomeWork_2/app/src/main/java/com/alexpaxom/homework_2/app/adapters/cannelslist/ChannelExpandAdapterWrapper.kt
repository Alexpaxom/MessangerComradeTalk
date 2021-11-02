package com.alexpaxom.homework_2.app.adapters.cannelslist

import androidx.recyclerview.widget.ConcatAdapter
import com.alexpaxom.homework_2.data.models.Channel
import com.alexpaxom.homework_2.data.models.ExpandedChanelGroup
import com.alexpaxom.homework_2.data.models.ExpandedChannelItem
import com.alexpaxom.homework_2.data.models.Topic

class ChannelExpandAdapterWrapper(
    val channelsListHoldersFactory: ChannelsListHoldersFactory,
    concatAdapterConfig: ConcatAdapter.Config = ConcatAdapter.Config.Builder()
        .setIsolateViewTypes(false)
        .build()

){
    val innerAdapter = ConcatAdapter(concatAdapterConfig, arrayListOf<ChannelsListAdapter>())

    var dataList: List<ExpandedChanelGroup>
        get() {
            val retDataList = arrayListOf<ExpandedChanelGroup>()
            for(adapter in innerAdapter.adapters) {
                when(adapter) {
                    is ChannelsListAdapter -> {
                        retDataList.add(
                            convertToExpandedGroup(adapter.dataList)
                                .apply { isExpanded = adapter.isExpanded }
                        )
                    }
                    else -> error("Wrong type adapter expected ChannelsListAdapter")
                }
            }

            return retDataList
        }
        set(value) {
            innerAdapter.adapters.forEach {
                innerAdapter.removeAdapter(it)
            }

            for (expandableChannel in value) {
                val adapter = ChannelsListAdapter(channelsListHoldersFactory)
                adapter.dataList = convertFromExpandedGroup(expandableChannel)
                adapter.isExpanded = expandableChannel.isExpanded
                innerAdapter.addAdapter(adapter)
            }
        }

    private fun convertFromExpandedGroup(group: ExpandedChanelGroup): List<ExpandedChannelItem> =
        listOf(group.channel, *group.topics.toTypedArray())


    private fun convertToExpandedGroup(list: List<ExpandedChannelItem>) : ExpandedChanelGroup {
        return ExpandedChanelGroup(
            channel = list[0] as Channel,
            topics = list.subList(1, list.size).map{it as Topic},
            isExpanded = false
        )
    }
}