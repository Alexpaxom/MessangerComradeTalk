package com.alexpaxom.homework_2.app.features.topicselector.adapters

import com.alexpaxom.homework_2.app.features.baseelements.adapters.BaseViewHolder
import com.alexpaxom.homework_2.data.models.TopicItem
import com.alexpaxom.homework_2.databinding.TopicInfoItemBinding

class TopicHolder(
    private val topicInfoItemBinding: TopicInfoItemBinding,
    private val onTopicItemClickListener: (topic: TopicItem) -> Unit
): BaseViewHolder<TopicItem>(topicInfoItemBinding) {

    init {
        itemView.setOnClickListener {
            (bindingAdapter as? TopicsListAdapter)?.let {
                onTopicItemClickListener(it.dataList[absoluteAdapterPosition] as TopicItem)
            }
        }
    }

    override fun bind(model: TopicItem) {
        topicInfoItemBinding.topicInfoName.text = model.name
    }
}