package com.alexpaxom.homework_2.app.adapters.cannelslist

import android.view.ViewGroup
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseDiffUtilAdapter
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseViewHolder
import com.alexpaxom.homework_2.data.models.ExpandedChanelGroup
import com.alexpaxom.homework_2.data.models.ListItem

class ChannelsListAdapter(
    val channelsListHoldersFactory: ChannelsListHoldersFactory
): BaseDiffUtilAdapter<ExpandedChanelGroup>(channelsListHoldersFactory) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ListItem> {
        return channelsListHoldersFactory(
            parent,
            if(viewType == HEADER_TYPE) R.layout.channel_info_item else R.layout.topic_info_item
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ListItem>, position: Int) {

        when(getItemViewType(position)){
            HEADER_TYPE-> {
                holder.bind(dataList.first().channel)
                onHeaderClick(holder)
            }
            LIST_TYPE->holder.bind(dataList.first().topics[position-1])
        }
    }

    private fun onHeaderClick(holder: BaseViewHolder<ListItem>) {

        holder.itemView.setOnClickListener {
            val element = dataList.first()
            element.isExpanded = !element.isExpanded

            if (element.isExpanded) {
                notifyItemRangeInserted(1, element.topics.size)
                notifyItemChanged(0)
            } else {
                notifyItemRangeRemoved(1, element.topics.size)
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
        return if(dataList.first().isExpanded)
            dataList.first().topics.size+1 // Header and inner list items
        else
            1 // Only header
    }

    companion object {
        private const val HEADER_TYPE = 0
        private const val LIST_TYPE = 1
    }

}