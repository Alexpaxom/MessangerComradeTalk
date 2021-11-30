package com.alexpaxom.homework_2.domain.repositories

import com.alexpaxom.homework_2.data.models.TopicItem
import io.reactivex.Single

interface TopicsRepository {
    fun getChannelTopics(channelId: Int): Single<List<TopicItem>>
}