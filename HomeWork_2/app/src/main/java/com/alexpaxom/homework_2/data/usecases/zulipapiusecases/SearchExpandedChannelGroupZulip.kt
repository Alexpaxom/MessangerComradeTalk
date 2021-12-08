package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.data.modelconverters.ChannelConverter
import com.alexpaxom.homework_2.data.modelconverters.TopicConverter
import com.alexpaxom.homework_2.data.models.ExpandedChanelGroup
import com.alexpaxom.homework_2.di.screen.ScreenScope
import com.alexpaxom.homework_2.domain.cache.helpers.CachedWrapper
import com.alexpaxom.homework_2.domain.entity.Channel
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.ChannelsZulipDataRepository
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.TopicsZulipDataRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@ScreenScope
class SearchExpandedChannelGroupZulip @Inject constructor(
    private val channelsRepository: ChannelsZulipDataRepository,
    private val topicsRepository: TopicsZulipDataRepository
) {

    private val topicConverter = TopicConverter()
    private val channelConverter = ChannelConverter()

    fun searchInSubscribedChannelGroups(
        searchString: String
    ): Observable<CachedWrapper<List<ExpandedChanelGroup>>> {
        return searchInChannels(channelsRepository.getSubscribedChannels(), searchString)
            .subscribeOn(Schedulers.io())
    }

    fun searchInAllChannelGroups(
        searchString: String
    ): Observable<CachedWrapper<List<ExpandedChanelGroup>>> {
        return searchInChannels(channelsRepository.getAllChannels(), searchString)
            .subscribeOn(Schedulers.io())
    }

    private fun searchInChannels(
        channels: Observable<CachedWrapper<List<Channel>>>,
        searchString: String
    ): Observable<CachedWrapper<List<ExpandedChanelGroup>>> {
            return channels
                .concatMapSingleDelayError { channelsWrap ->
                    loadChannelGroups(channelsWrap)
                        .subscribeOn(Schedulers.io())
                        .filterSearch(searchString)
                        .map { searchResult->
                            when(channelsWrap) {
                                is CachedWrapper.CachedData -> CachedWrapper.CachedData(searchResult)
                                is CachedWrapper.OriginalData -> CachedWrapper.OriginalData(searchResult)
                            }
                        }
                }
    }

    private fun loadChannelGroups(channels: CachedWrapper<List<Channel>>): Single<List<ExpandedChanelGroup>> {
        return Observable.fromIterable(channels.data)
            .flatMap { channel ->
                topicsRepository.getChannelTopics(
                    channelId = channel.streamId,
                    useCache = channels is CachedWrapper.CachedData,
                    useApi = channels is CachedWrapper.OriginalData
                ).map { topicsWrap ->
                    ExpandedChanelGroup (
                        channel = channelConverter.convert(channel),
                        topics = topicsWrap.data.map{ topicConverter.convert(it) },
                    )
                }
            }
            .toList()
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
}