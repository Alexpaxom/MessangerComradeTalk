package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.data.models.ExpandedChanelGroup
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.ChannelsZulipDataRepository
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.TopicsZulipDataRepository
import io.reactivex.Single

class SearchExpandedChannelGroupZulip(
    val channelsRepository: ChannelsZulipDataRepository = ChannelsZulipDataRepository(),
    val topicsRepository: TopicsZulipDataRepository = TopicsZulipDataRepository()
) {

    fun searchInSubscribedChannelGroups(searchString: String): Single<List<ExpandedChanelGroup>> {
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
        return result
    }

    fun searchInAllChannelGroups(searchString: String): Single<List<ExpandedChanelGroup>> {
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