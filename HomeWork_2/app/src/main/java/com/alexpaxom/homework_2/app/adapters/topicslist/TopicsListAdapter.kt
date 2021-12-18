package com.alexpaxom.homework_2.app.adapters.topicslist

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseDiffUtilAdapter
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseHolderFactory
import com.alexpaxom.homework_2.data.models.TopicItem

class TopicsListAdapter(
    holdersFactory: BaseHolderFactory
): BaseDiffUtilAdapter<TopicItem>(holdersFactory) {

    init {
        diffUtil = AsyncListDiffer(this, TopicsListAdapter.TopicDiffUtil())
    }

    class TopicDiffUtil: DiffUtil.ItemCallback<TopicItem>() {
        override fun areItemsTheSame(oldItem: TopicItem, newItem: TopicItem): Boolean {
            return oldItem.id == newItem.id && oldItem.channelId == newItem.channelId
        }

        override fun areContentsTheSame(oldItem: TopicItem, newItem: TopicItem): Boolean {
            return oldItem == newItem
        }
    }

}