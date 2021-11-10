package com.alexpaxom.homework_2.app.adapters.cannelslist

import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseViewHolder
import com.alexpaxom.homework_2.data.models.TopicItem
import com.alexpaxom.homework_2.databinding.TopicInfoItemBinding

class TopicInfoHolder(
    private val topicInfoItemBinding: TopicInfoItemBinding,
    private val onExpandableChannelItemClickListener: (position: Int) -> Unit
): BaseViewHolder<TopicItem>(topicInfoItemBinding) {

    init {
        itemView.setOnClickListener {
            onExpandableChannelItemClickListener(bindingAdapterPosition)
        }
    }

    override fun bind(model: TopicItem) {
        topicInfoItemBinding.topicInfoName.text = model.name
        topicInfoItemBinding.topicInfoMessageCount.text = model.countMessages.toString()
    }
}