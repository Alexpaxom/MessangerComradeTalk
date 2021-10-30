package com.alexpaxom.homework_2.app.adapters.cannelslist

import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseViewHolder
import com.alexpaxom.homework_2.data.models.Topic
import com.alexpaxom.homework_2.databinding.TopicInfoItemBinding

class TopicInfoHolder(
    val topicInfoItemBinding: TopicInfoItemBinding,
    val onTopicClickListener: (topicPos: Int) -> Unit
): BaseViewHolder<Topic>(topicInfoItemBinding) {

    init {
        itemView.setOnClickListener {
            onTopicClickListener(bindingAdapterPosition)
        }
    }

    override fun bind(model: Topic) {
        topicInfoItemBinding.topicInfoName.text = model.name
        topicInfoItemBinding.topicInfoMessageCount.text = model.countMessages.toString()
    }
}