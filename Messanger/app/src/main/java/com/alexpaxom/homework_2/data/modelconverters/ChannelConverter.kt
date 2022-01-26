package com.alexpaxom.homework_2.data.modelconverters

import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.data.models.ChannelItem
import com.alexpaxom.homework_2.domain.entity.Channel

class ChannelConverter {
    fun convert(channel: Channel): ChannelItem{
        return ChannelItem (
            typeId = R.layout.channel_info_item,
            id = channel.streamId,
            name = channel.name ?: "not name",
            isExpanded = false
        )
    }
}