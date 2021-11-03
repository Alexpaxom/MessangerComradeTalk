package com.alexpaxom.homework_2.app.adapters.cannelslist

import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseViewHolder
import com.alexpaxom.homework_2.data.models.Channel
import com.alexpaxom.homework_2.databinding.ChannelInfoItemBinding

class ChannelInfoHolder(
    private val channelInfoItemBinding: ChannelInfoItemBinding,
    private val onMessageClickListener: (topicPos: Int) -> Unit
): BaseViewHolder<Channel>(channelInfoItemBinding) {

    init {
        channelInfoItemBinding.channelInfoExpandList.setOnClickListener {
            (bindingAdapter as ChannelsListAdapter).apply {
                dataList.indexOfLast { it.channel.id == innerList[this@ChannelInfoHolder.bindingAdapterPosition].id }
                    ?.let{ groupPosition->
                    dataList[groupPosition].channel.isExpanded = !dataList[groupPosition].channel.isExpanded
                    updateItem(groupPosition, dataList[groupPosition])
                }
                notifyItemChanged(this@ChannelInfoHolder.bindingAdapterPosition)
            }
        }

        itemView.setOnClickListener {
            onMessageClickListener(bindingAdapterPosition)
        }

    }

    override fun bind(model: Channel) {
        channelInfoItemBinding.channelInfoName.text = model.name
        if(model.isExpanded)
            channelInfoItemBinding.channelInfoExpandList.setImageResource(R.drawable.ic_close_list)
        else
            channelInfoItemBinding.channelInfoExpandList.setImageResource(R.drawable.ic_open_list)
    }
}