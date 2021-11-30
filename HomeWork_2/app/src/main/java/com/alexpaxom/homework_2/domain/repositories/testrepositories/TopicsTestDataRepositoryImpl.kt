package com.alexpaxom.homework_2.domain.repositories.testrepositories

import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.data.models.TopicItem
import com.alexpaxom.homework_2.domain.repositories.TopicsRepository
import io.reactivex.Single
import kotlin.random.Random

class TopicsTestDataRepositoryImpl: TopicsRepository {
    private val maxCountRandomTopic = 100
    private var randomSeed = 100


    override fun getChannelTopics(channelId: Int): Single<List<TopicItem>> {
        val random = Random(randomSeed++)

        val topicsResult = arrayListOf<TopicItem>()
        repeat(random.nextInt(maxCountRandomTopic)) {
            topicsResult.add(
                TopicItem(
                    typeId = R.layout.topic_info_item,
                    id = channelId + it,
                    channelId = channelId,
                    name = "Topic №${channelId + it}",
                )
            )
        }
        return Single.just(topicsResult)
    }

    fun resetRandomSeed() {
        randomSeed = 100
    }
}