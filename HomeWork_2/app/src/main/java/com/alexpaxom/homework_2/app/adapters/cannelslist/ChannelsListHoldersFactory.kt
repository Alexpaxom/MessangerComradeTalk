package com.alexpaxom.homework_2.app.adapters.cannelslist

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseHolderFactory
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseViewHolder
import com.alexpaxom.homework_2.data.models.ChannelItem
import com.alexpaxom.homework_2.data.models.TopicItem
import com.alexpaxom.homework_2.databinding.ChannelInfoItemBinding
import com.alexpaxom.homework_2.databinding.TopicInfoItemBinding

class ChannelsListHoldersFactory(
    private val onExpandableChannelItemClickListener: (position: Int) -> Unit
): BaseHolderFactory() {
    override fun createViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when(viewType) {
            R.layout.topic_info_item -> TopicInfoHolder(
                TopicInfoItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                ),
                onExpandableChannelItemClickListener
            )
            R.layout.channel_info_item -> ChannelInfoHolder(
                ChannelInfoItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                ),
                onExpandableChannelItemClickListener
            )
            else -> error("Bad type channel list holder")
        }
    }
}