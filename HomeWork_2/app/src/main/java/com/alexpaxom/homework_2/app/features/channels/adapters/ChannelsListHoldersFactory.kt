package com.alexpaxom.homework_2.app.features.channels.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.features.baseelements.adapters.BaseHolderFactory
import com.alexpaxom.homework_2.app.features.baseelements.adapters.BaseViewHolder
import com.alexpaxom.homework_2.data.models.ChannelItem
import com.alexpaxom.homework_2.data.models.TopicItem
import com.alexpaxom.homework_2.databinding.ChannelInfoItemBinding
import com.alexpaxom.homework_2.databinding.TopicInfoItemBinding

class ChannelsListHoldersFactory(
    private val onExpandableChannelItemClickListener: (channel: ChannelItem) -> Unit,
    private val onExpandableTopicItemClickListener: (topic: TopicItem) -> Unit,
    private val onExpandChannelListener: ((channel: ChannelItem) -> Unit)? = null,
    private val onExpandableChannelLongClickListener: ((channel: ChannelItem) -> Unit)? = null,
    private val onExpandableTopicLongClickListener: ((topic: TopicItem) -> Unit)? = null,
): BaseHolderFactory() {
    override fun createViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when(viewType) {
            R.layout.topic_info_item -> TopicInfoHolder(
                topicInfoItemBinding = TopicInfoItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                ),
                onExpandableTopicItemClickListener = onExpandableTopicItemClickListener,
                onExpandableTopicLongClickListener = onExpandableTopicLongClickListener
            )
            R.layout.channel_info_item -> ChannelInfoHolder(
                channelInfoItemBinding = ChannelInfoItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                ),
                channelClickListener = onExpandableChannelItemClickListener,
                onExpandChannelListener = onExpandChannelListener,
                channelLongClickListener = onExpandableChannelLongClickListener
            )
            else -> error("Bad type channel list holder")
        }
    }
}