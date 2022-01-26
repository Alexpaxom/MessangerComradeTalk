package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.data.modelconverters.TopicConverter
import com.alexpaxom.homework_2.data.models.TopicItem
import com.alexpaxom.homework_2.di.screen.ScreenScope
import com.alexpaxom.homework_2.domain.cache.helpers.CachedWrapper
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.TopicsZulipDataRepository
import io.reactivex.Observable
import javax.inject.Inject

@ScreenScope
class LoadTopicsUseCaseZulip @Inject constructor(
    private val topicsRepository: TopicsZulipDataRepository
) {

    private val topicConverter = TopicConverter()

    fun searchTopics(channelId: Int, searchString: String): Observable<CachedWrapper<List<TopicItem>>> {
         return topicsRepository.getChannelTopics(channelId)
            .map {  topicsWrap ->
                val searchResult = topicsWrap.data.filter { it.name.contains(searchString, ignoreCase = true) }
                    .map { topicConverter.convert(it) }


                when(topicsWrap) {
                    is CachedWrapper.CachedData -> CachedWrapper.CachedData(searchResult)
                    is CachedWrapper.OriginalData -> CachedWrapper.OriginalData(searchResult)
                }
            }
    }
}