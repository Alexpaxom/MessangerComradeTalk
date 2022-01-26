package com.alexpaxom.homework_2.app.features.channels.adapters

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.alexpaxom.homework_2.app.features.baseelements.adapters.BaseDiffUtilAdapter
import com.alexpaxom.homework_2.app.features.baseelements.adapters.BaseViewHolder
import com.alexpaxom.homework_2.data.models.ExpandedChanelGroup
import com.alexpaxom.homework_2.data.models.ExpandedChannelItem
import com.alexpaxom.homework_2.data.models.ListItem

class ChannelsListAdapter(
    channelsListHoldersFactory: ChannelsListHoldersFactory,
): BaseDiffUtilAdapter<ExpandedChanelGroup>(channelsListHoldersFactory) {

    override var dataList: List<ExpandedChanelGroup> = listOf()
        set(list) {
            field = list
            innerList = list.flatMap { expandableChannel ->
                expandableChannel.toExpandableItemList(expandableChannel.channel.isExpanded)
            }
        }

    internal var innerList: List<ExpandedChannelItem>
        get() = itemsDiffUtil.currentList
        set(value) {
            itemsDiffUtil.submitList(value)
        }

    var itemsDiffUtil: AsyncListDiffer<ExpandedChannelItem> = AsyncListDiffer(this, ExpandedChannelItemItemsDiffUtil())


    override fun onBindViewHolder(holder: BaseViewHolder<ListItem>, position: Int) {
        holder.bind(innerList[position])
    }


    override fun getItemViewType(position: Int): Int {
        return innerList[position].typeId
    }

    override fun getItemCount(): Int {
        return innerList.size
    }


    override fun updateItem(pos: Int, newItem: ExpandedChanelGroup) {
        val list: MutableList<ExpandedChanelGroup> = mutableListOf()
        list.addAll(dataList)
        list[pos] = newItem

        dataList = list
    }

    override fun addItem(newItem: ExpandedChanelGroup) {
        val list: MutableList<ExpandedChanelGroup> = mutableListOf()
        list.addAll(dataList)
        list.add(newItem)

        dataList = list
    }

    override fun removeItem(pos: Int) {
        val list: MutableList<ExpandedChanelGroup> = mutableListOf()
        list.addAll(dataList)
        list.removeAt(pos)

        dataList = list
    }

    override fun removeAll() {
        dataList = listOf()
    }


    class ExpandedChannelItemItemsDiffUtil: DiffUtil.ItemCallback<ExpandedChannelItem>() {
        override fun areItemsTheSame(oldItem: ExpandedChannelItem, newItem: ExpandedChannelItem): Boolean {
            return oldItem.id == newItem.id && oldItem.typeId == newItem.typeId
        }

        override fun areContentsTheSame(oldItem: ExpandedChannelItem, newItem: ExpandedChannelItem): Boolean {
            return oldItem == newItem
        }

    }

}