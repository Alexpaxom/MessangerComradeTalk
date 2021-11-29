package com.alexpaxom.homework_2.domain.repositories.zulipapirepositories

import com.alexpaxom.homework_2.domain.cache.GetDatabaseObject
import com.alexpaxom.homework_2.domain.cache.daos.ChannelsDAO
import com.alexpaxom.homework_2.domain.cache.helpers.CachedWrapper
import com.alexpaxom.homework_2.domain.entity.Channel
import com.alexpaxom.homework_2.domain.remote.ChannelsZulipApiRequests
import io.reactivex.Observable

class ChannelsZulipDataRepository(
    private val channelsZulipApiRequests: ChannelsZulipApiRequests =
        GetRetrofitObject.retrofit.create(ChannelsZulipApiRequests::class.java),
    private val channelsDAO: ChannelsDAO = GetDatabaseObject.channelsDao
) {

    fun getSubscribedChannels(
        useCache: Boolean = true,
        refreshCache: Boolean = true,
    ): Observable<CachedWrapper<List<Channel>>> {

        return Observable.create { emitter->
            // Возвращаем кэш если если он есть
            if(useCache) {
                val channels = channelsDAO.getAllSubscribed()
                if(channels.isNotEmpty())
                    emitter.onNext(CachedWrapper.CachedData(channels))
            }

            try {
                // Запрашиваем данные с сервера и возвращаем следом за кэшем
                val channels = channelsZulipApiRequests.getSubscribedStreams().execute().body()?.subscriptions ?: listOf()
                emitter.onNext(CachedWrapper.OriginalData(channels))

                // обновляем кэш
                if(refreshCache)
                    channelsDAO.deleteAllSubscribed()
                channelsDAO.insertAll(channels.map{ it.copy(inSubscribes = true)} )
            }
            catch (e: Exception) {
                emitter.tryOnError(e)
            }
        }
    }

    fun getAllChannels(
        useCache: Boolean = true,
        refreshCache: Boolean = true,
    ): Observable<CachedWrapper<List<Channel>>> {

        return Observable.create { emitter->
            // Возвращаем кэш если если он есть
            if(useCache) {
                val channels = channelsDAO.getAll()
                if(channels.isNotEmpty())
                    emitter.onNext(CachedWrapper.CachedData(channels))
            }

            try {
                // Запрашиваем данные с сервера и возвращаем следом за кэшем
                val channels = channelsZulipApiRequests.getAllStreams().execute().body()?.streams ?: listOf()
                emitter.onNext(CachedWrapper.OriginalData(channels))

                // обновляем кэш
                if(refreshCache)
                    channelsDAO.deleteAllChannels()
                channelsDAO.insertAll(channels.map{ it.copy(inSubscribes = false)})

                emitter.onComplete()
            }
            catch (e: Exception) {
                emitter.tryOnError(e)
            }
        }
    }
}