package com.alexpaxom.homework_2.app.adapters.cannelslist

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseHolderFactory
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseViewHolder
import com.alexpaxom.homework_2.databinding.ChannelInfoItemBinding
import com.alexpaxom.homework_2.databinding.TopicInfoItemBinding

class ChannelsListHoldersFactory(
    val onTopicClickListener: (topicPos: Int) -> Unit
): BaseHolderFactory() {
    override fun createViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when(viewType) {
            R.layout.topic_info_item -> TopicInfoHolder(
                TopicInfoItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                ),
                onTopicClickListener
            )
            R.layout.channel_info_item -> ChannelInfoHolder(
                ChannelInfoItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
            else -> error("Bad type channel list holder")
        }
    }
}