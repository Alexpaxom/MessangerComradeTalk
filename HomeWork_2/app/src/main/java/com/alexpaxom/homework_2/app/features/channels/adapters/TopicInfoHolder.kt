package com.alexpaxom.homework_2.app.features.channels.adapters

import com.alexpaxom.homework_2.app.features.baseelements.adapters.BaseViewHolder
import com.alexpaxom.homework_2.data.models.TopicItem
import com.alexpaxom.homework_2.databinding.TopicInfoItemBinding

class TopicInfoHolder(
    private val topicInfoItemBinding: TopicInfoItemBinding,
    private val onExpandableTopicItemClickListener: (topic: TopicItem) -> Unit,
    private val onExpandableTopicLongClickListener: ((topic: TopicItem) -> Unit)? = null,
): BaseViewHolder<TopicItem>(topicInfoItemBinding) {

    init {
        itemView.setOnClickListener {
            (bindingAdapter as? ChannelsListAdapter)?.let {
                onExpandableTopicItemClickListener(it.innerList[bindingAdapterPosition] as TopicItem)
            }
        }

        itemView.setOnLongClickListener() {
            (bindingAdapter as? ChannelsListAdapter)?.let {
                onExpandableTopicLongClickListener?.invoke(it.innerList[bindingAdapterPosition] as TopicItem)
            }

            true
        }
    }

    override fun bind(model: TopicItem) {
        topicInfoItemBinding.topicInfoName.text = model.name
    }
}