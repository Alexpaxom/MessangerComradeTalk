package com.alexpaxom.homework_2.domain.repositories.zulipapirepositories

import com.alexpaxom.homework_2.data.modelconverters.TopicConverter
import com.alexpaxom.homework_2.data.models.TopicItem
import com.alexpaxom.homework_2.domain.remote.ChannelsZulipApiRequests
import com.alexpaxom.homework_2.domain.repositories.TopicsRepository
import io.reactivex.Single

class TopicsZulipDataRepositoryImpl: TopicsRepository {

    private val retrofit = GetRetrofitObject.retrofit
    private val topicsZulipApiRequests = retrofit.create(ChannelsZulipApiRequests::class.java)
    private val topicConverter = TopicConverter()
    override fun getChannelTopics(channelId: Int): Single<List<TopicItem>> {
        return topicsZulipApiRequests.getTopicsByStreamId(channelId)
            .map { topicsWtapper ->
                topicsWtapper.topics.map {  topic ->
                    topicConverter.convert(topic, channelId)
                }
            }
    }

}