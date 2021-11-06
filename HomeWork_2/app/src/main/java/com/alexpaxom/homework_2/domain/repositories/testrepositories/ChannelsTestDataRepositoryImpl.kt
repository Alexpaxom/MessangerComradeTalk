package com.alexpaxom.homework_2.domain.repositories.testrepositories

import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.data.models.ChannelItem
import com.alexpaxom.homework_2.domain.repositories.ChannelsRepository
import io.reactivex.Single
import kotlin.random.Random

class ChannelsTestDataRepositoryImpl: ChannelsRepository {
    private val randomSeed = 100
    private val maxCountElements = 10

    override fun getChannels(): Single<List<ChannelItem>> {
        val random = Random(randomSeed)

        val channelsList: ArrayList<ChannelItem> = arrayListOf()

        var nextChannelId = 0

        repeat(random.nextInt(maxCountElements)) {
            channelsList += ChannelItem(
                typeId = R.layout.channel_info_item,
                id = nextChannelId,
                name = "Channel $it"
            )

            nextChannelId+=1000
        }

        return Single.just(channelsList)
    }
}