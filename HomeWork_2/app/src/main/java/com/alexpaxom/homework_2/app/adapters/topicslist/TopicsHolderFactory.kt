package com.alexpaxom.homework_2.app.adapters.topicslist

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseHolderFactory
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseViewHolder
import com.alexpaxom.homework_2.app.adapters.cannelslist.TopicInfoHolder
import com.alexpaxom.homework_2.data.models.TopicItem
import com.alexpaxom.homework_2.databinding.TopicInfoItemBinding

class TopicsHolderFactory(
    private val onTopicItemClickListener: (topic: TopicItem) -> Unit
): BaseHolderFactory() {
    override fun createViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when(viewType) {
            R.layout.topic_info_item -> TopicHolder(
                TopicInfoItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                ),
                onTopicItemClickListener
            )
            else -> error("Bad type topic list holder")
        }
    }
}