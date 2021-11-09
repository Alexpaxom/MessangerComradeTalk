package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.data.models.ExpandedChanelGroup
import com.alexpaxom.homework_2.data.usecases.SearchExpandedChannelGroup
import com.alexpaxom.homework_2.domain.repositories.ChannelsRepository
import com.alexpaxom.homework_2.domain.repositories.TopicsRepository
import com.alexpaxom.homework_2.domain.repositories.testrepositories.TopicsTestDataRepositoryImpl
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.ChannelsZulipDataRepositoryImpl
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.TopicsZulipDataRepositoryImpl
import io.reactivex.Single

class SearchExpandedChannelGroupZulipImpl(
    val channelsRepository: ChannelsRepository = ChannelsZulipDataRepositoryImpl(),
    val topicsRepository: TopicsRepository = TopicsZulipDataRepositoryImpl()
): SearchExpandedChannelGroup {

    override fun searchInSubscribedChannelGroups(searchString: String): Single<List<ExpandedChanelGroup>> {
        val result = channelsRepository.getSubscribedChannels().flatMap { channels ->
            val expandedChanelGroupList = channels.map { channel ->
                topicsRepository.getChannelTopics(channel.id).map{ topics ->
                    ExpandedChanelGroup (
                        channel = channel,
                        topics = topics,
                    )
                }
            }

            expandedChanelGroupList.zipSingles().filterSearch(searchString)
        }
        (topicsRepository as? TopicsTestDataRepositoryImpl)?.apply { resetRandomSeed() }
        return result
    }

    override fun searchInAllChannelGroups(searchString: String): Single<List<ExpandedChanelGroup>> {
        val result = channelsRepository.getAllChannels().flatMap { channels ->
            val expandedChanelGroupList = channels.map { channel ->
                topicsRepository.getChannelTopics(channel.id).map{ topics ->
                    ExpandedChanelGroup (
                        channel = channel,
                        topics = topics,
                    )
                }
            }

            expandedChanelGroupList.zipSingles().filterSearch(searchString)
        }
        (topicsRepository as? TopicsTestDataRepositoryImpl)?.apply { resetRandomSeed() }
        return result
    }

    private fun Single<List<ExpandedChanelGroup>>.filterSearch(searchString: String): Single<List<ExpandedChanelGroup>> {
        return this.map { itemsList ->
            itemsList.filter { item ->
                item.channel.name.contains(searchString, ignoreCase = true)
                        || item.topics.any{ it.name.contains(searchString, ignoreCase = true) }
            }.map { item ->
                item.copy (
                    topics = item.topics.filter { it.name.contains(searchString, ignoreCase = true) }
                )
            }
        }
    }

    private fun <T: Any> List<Single<T>>.zipSingles(): Single<List<T>> {
        if (isEmpty()) return Single.just(emptyList())

        return Single.zip(this) {
            @Suppress("UNCHECKED_CAST")
            return@zip (it as Array<T>).toList()
        }
    }
}