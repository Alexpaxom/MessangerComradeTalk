package com.alexpaxom.homework_2.domain.repositories.zulipapirepositories

import com.alexpaxom.homework_2.domain.cache.GetDatabaseObject
import com.alexpaxom.homework_2.domain.cache.helpers.CachedWrapper
import com.alexpaxom.homework_2.domain.entity.Topic
import com.alexpaxom.homework_2.domain.remote.ChannelsZulipApiRequests
import io.reactivex.Observable

class TopicsZulipDataRepository {

    private val retrofit = GetRetrofitObject.retrofit
    private val topicsZulipApiRequests = retrofit.create(ChannelsZulipApiRequests::class.java)
    private val topicsDAO = GetDatabaseObject.topicsDao

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
                // Запрашиваем данные с сервера и возвращаем следом за кэшем
                val topics = topicsZulipApiRequests.getTopicsByStreamId(channelId).execute().body()?.topics ?: listOf()
                emitter.onNext(CachedWrapper.OriginalData(topics))

                // обновляем кэш
                if(refreshCache)
                    topicsDAO.insertAll(topics.map { it.copy(channelId = channelId) })
            }

            emitter.onComplete()
        }
    }

}