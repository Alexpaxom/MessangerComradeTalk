package com.alexpaxom.homework_2.domain.repositories.zulipapirepositories

import com.alexpaxom.homework_2.di.screen.ScreenScope
import com.alexpaxom.homework_2.domain.cache.daos.ChannelsDAO
import com.alexpaxom.homework_2.domain.cache.helpers.CachedWrapper
import com.alexpaxom.homework_2.domain.entity.Channel
import com.alexpaxom.homework_2.domain.remote.ChannelsZulipApiRequests
import io.reactivex.Observable
import javax.inject.Inject

@ScreenScope
class ChannelsZulipDataRepository @Inject constructor(
    private val channelsZulipApiRequests: ChannelsZulipApiRequests,
    private val channelsDAO: ChannelsDAO
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
                val channels = channelsZulipApiRequests.getSubscribedStreams()
                    .execute()
                    .body()
                    ?.subscriptions
                    ?.map{ it.copy(inSubscribes = true)} ?: listOf()

                emitter.onNext(CachedWrapper.OriginalData(channels))

                // обновляем кэш
                if(refreshCache)
                    channelsDAO.deleteAllSubscribed()
                channelsDAO.insertAll(channels )
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

                // Получаем id стримов на которые уже подписаны
                val subscribedIds = channelsZulipApiRequests.getSubscribedStreams()
                    .execute()
                    .body()
                    ?.subscriptions?.map {it.streamId}?.toSet() ?: setOf()

                // Запрашиваем данные с сервера и возвращаем следом за кэшем
                // Попутно отмечаем те стримы на которые уже подписаны
                val channels = channelsZulipApiRequests.getAllStreams()
                    .execute()
                    .body()
                    ?.streams
                    ?.map{ it.copy(inSubscribes = it.streamId in subscribedIds) } ?: listOf()


                emitter.onNext(CachedWrapper.OriginalData(channels))

                // обновляем кэш
                if(refreshCache)
                    channelsDAO.deleteAllChannels()
                channelsDAO.insertAll(channels)

                emitter.onComplete()
            }
            catch (e: Exception) {
                emitter.tryOnError(e)
            }
        }
    }
}