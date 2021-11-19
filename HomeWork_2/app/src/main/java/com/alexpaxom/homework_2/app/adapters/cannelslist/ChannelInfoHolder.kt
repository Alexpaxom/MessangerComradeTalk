package com.alexpaxom.homework_2.app.adapters.cannelslist

import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseViewHolder
import com.alexpaxom.homework_2.data.models.ChannelItem
import com.alexpaxom.homework_2.databinding.ChannelInfoItemBinding

class ChannelInfoHolder(
    private val channelInfoItemBinding: ChannelInfoItemBinding,
    private val channelClickListener: (channel: ChannelItem) -> Unit,
): BaseViewHolder<ChannelItem>(channelInfoItemBinding) {

    init {
        // обрабатываем нажатие на канал (показываем/скрываем топики)
        channelInfoItemBinding.channelInfoLayout.setOnClickListener {
            (bindingAdapter as ChannelsListAdapter).apply {
                val groupItemPosition =
                dataList.indexOfLast {
                    it.channel.id == innerList[this@ChannelInfoHolder.bindingAdapterPosition].id
                }

                if(groupItemPosition != -1) {
                    dataList[groupItemPosition].let { groupItem ->
                        val groupItemInvertIsExpanded =  groupItem.copy(
                            channel = groupItem.channel.copy(
                                isExpanded = !groupItem.channel.isExpanded
                            )
                        )
                        updateItem(groupItemPosition, groupItemInvertIsExpanded)
                        // Внешний обработчик нажатия
                        channelClickListener(groupItemInvertIsExpanded.channel)
                    }
                }
            }
        }
    }

    override fun bind(model: ChannelItem) {
        channelInfoItemBinding.channelInfoName.text = model.name
        if(model.isExpanded)
            channelInfoItemBinding.channelInfoExpandList.setImageResource(R.drawable.ic_close_list)
        else
            channelInfoItemBinding.channelInfoExpandList.setImageResource(R.drawable.ic_open_list)
    }
}