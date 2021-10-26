package com.alexpaxom.homework_2.app.adapters.cannelslist

import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseViewHolder
import com.alexpaxom.homework_2.data.models.Channel
import com.alexpaxom.homework_2.databinding.ChannelInfoItemBinding

class ChannelInfoHolder(
    val chanelInfoItemBinding: ChannelInfoItemBinding
): BaseViewHolder<Channel>(chanelInfoItemBinding) {
    override fun bind(model: Channel) {
        chanelInfoItemBinding.channelInfoName.text = model.name
    }
}