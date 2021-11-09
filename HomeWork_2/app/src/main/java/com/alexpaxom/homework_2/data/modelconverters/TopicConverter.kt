package com.alexpaxom.homework_2.data.modelconverters

import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.data.models.TopicItem
import com.alexpaxom.homework_2.domain.entity.Topic

class TopicConverter {
    fun convert(topic: Topic, channelId: Int): TopicItem {
        return TopicItem (
            typeId = R.layout.topic_info_item,
            id = topic.name.hashCode(),
            channelId = channelId,
            name = topic.name,
            countMessages = 0
        )
    }
}