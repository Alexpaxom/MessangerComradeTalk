package com.alexpaxom.homework_2.app.features.channels.adapters

import com.alexpaxom.homework_2.app.features.baseelements.adapters.BaseViewHolder
import com.alexpaxom.homework_2.data.models.TopicItem
import com.alexpaxom.homework_2.databinding.TopicInfoItemBinding

class TopicInfoHolder(
    private val topicInfoItemBinding: TopicInfoItemBinding,
    private val onExpandableTopicItemClickListener: (topic: TopicItem) -> Unit
): BaseViewHolder<TopicItem>(topicInfoItemBinding) {

    init {
        itemView.setOnClickListener {
            (bindingAdapter as? ChannelsListAdapter)?.let {
                onExpandableTopicItemClickListener(it.innerList[bindingAdapterPosition] as TopicItem)
            }
        }
    }

    override fun bind(model: TopicItem) {
        topicInfoItemBinding.topicInfoName.text = model.name
        topicInfoItemBinding.topicInfoMessageCount.text = model.countMessages.toString()
    }
}