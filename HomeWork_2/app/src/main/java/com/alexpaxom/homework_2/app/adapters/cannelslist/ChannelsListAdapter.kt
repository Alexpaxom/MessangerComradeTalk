package com.alexpaxom.homework_2.app.adapters.cannelslist

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseDiffUtilAdapter
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseViewHolder
import com.alexpaxom.homework_2.data.models.ExpandedChanelGroup
import com.alexpaxom.homework_2.data.models.ExpandedChannelItem
import com.alexpaxom.homework_2.data.models.ListItem
import com.alexpaxom.homework_2.data.models.Message

class ChannelsListAdapter(
    val channelsListHoldersFactory: ChannelsListHoldersFactory,
): BaseDiffUtilAdapter<ExpandedChannelItem>(channelsListHoldersFactory) {

    var isExpanded = false

    init {
        diffUtil = AsyncListDiffer(this, ExpandedChannelItemItemsDiffUtil())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ListItem> {
        return channelsListHoldersFactory(
            parent,
            if(viewType == HEADER_TYPE) R.layout.channel_info_item else R.layout.topic_info_item
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ListItem>, position: Int) {

        when(getItemViewType(position)){
            HEADER_TYPE-> {
                holder.bind(dataList[0])
                onHeaderClick(holder)
            }
            LIST_TYPE->holder.bind(dataList[position])
        }
    }

    private fun onHeaderClick(holder: BaseViewHolder<ListItem>) {

        holder.itemView.setOnClickListener {
            isExpanded = !isExpanded

            if (isExpanded) {
                notifyItemRangeInserted(1, dataList.size-1)
                notifyItemChanged(0)
            } else {
                notifyItemRangeRemoved(1, dataList.size-1)
                notifyItemChanged(0)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0)
            HEADER_TYPE
        else
            LIST_TYPE
    }

    override fun getItemCount(): Int {
        return if(isExpanded)
            dataList.size-1 // Header and inner list items
        else
            1 // Only header
    }

    class ExpandedChannelItemItemsDiffUtil: DiffUtil.ItemCallback<ExpandedChannelItem>() {
        override fun areItemsTheSame(oldItem: ExpandedChannelItem, newItem: ExpandedChannelItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ExpandedChannelItem, newItem: ExpandedChannelItem): Boolean {
            return oldItem == newItem
        }

    }

    companion object {
        private const val HEADER_TYPE = 0
        private const val LIST_TYPE = 1
    }

}