package com.alexpaxom.homework_2.domain.repositories.zulipapirepositories

import com.alexpaxom.homework_2.domain.cache.GetDatabaseObject
import com.alexpaxom.homework_2.domain.cache.daos.TopicsDAO
import com.alexpaxom.homework_2.domain.cache.helpers.CachedWrapper
import com.alexpaxom.homework_2.domain.entity.Topic
import com.alexpaxom.homework_2.domain.remote.ChannelsZulipApiRequests
import io.reactivex.Observable

class TopicsZulipDataRepository(
    private val topicsZulipApiRequests: ChannelsZulipApiRequests =
        GetRetrofitObject.retrofit.create(ChannelsZulipApiRequests::class.java),
    private val topicsDAO: TopicsDAO = GetDatabaseObject.topicsDao
) {

    fun getChannelTopics(
        channelId: Int,
        useCache: Boolean = true,
        refreshCache: Boolean = true,
        useApi: Boolean = true
    ): Observable<CachedWrapper<List<Topic>>> {

        return Observable.create { emitter->
            // Возвращаем кэш если если он есть
            if(useCache) {
                val topics = topicsDAO.getTopicByChannelId(channelId)
                if(topics.isNotEmpty())
                    emitter.onNext(CachedWrapper.CachedData(topics))
            }

            if(useApi)
            {
                try {
                    // Запрашиваем данные с сервера и возвращаем следом за кэшем
                    val topics =
                        topicsZulipApiRequests
                            .getTopicsByStreamId(channelId)
                            .execute()
                            .body()
                            ?.topics
                            ?.map { it.copy(channelId = channelId) } ?: listOf()

                    emitter.onNext(CachedWrapper.OriginalData(topics))

                    // обновляем кэш
                    if(refreshCache)
                        topicsDAO.insertAll(topics)

                }
                catch (e: Exception) {
                    emitter.tryOnError(e)
                }
            }

            emitter.onComplete()
        }
    }

}