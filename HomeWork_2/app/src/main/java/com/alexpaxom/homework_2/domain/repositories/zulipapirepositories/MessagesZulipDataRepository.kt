package com.alexpaxom.homework_2.domain.repositories.zulipapirepositories

import com.alexpaxom.homework_2.domain.cache.GetDatabaseObject
import com.alexpaxom.homework_2.domain.cache.daos.MessagesDAO
import com.alexpaxom.homework_2.domain.cache.helpers.CachedWrapper
import com.alexpaxom.homework_2.domain.entity.Message
import com.alexpaxom.homework_2.domain.entity.ReactionResult
import com.alexpaxom.homework_2.domain.entity.SendResult
import com.alexpaxom.homework_2.domain.remote.MessagesZulipApiRequests
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*
import kotlin.concurrent.schedule

class MessagesZulipDataRepository(
    private val messagesZulipApiRequests: MessagesZulipApiRequests =
        GetRetrofitObject.retrofit.create(MessagesZulipApiRequests::class.java),
    private val messagesDao: MessagesDAO = GetDatabaseObject.messagesDao
) {

    private fun getMessages(
        messageId: Long,
        numBefore:Int,
        numAfter:Int,
        filter: NarrowParams,
        useCache: Boolean = false,
        refreshCache: Boolean = false,
    ): Observable<CachedWrapper<List<Message>>> {
        return Observable.create { emiter->
           // Возвращаем кэш если если он есть
            if(useCache) {
                messagesDao.getAll(filter.streamId, filter.topicName).let { msgs ->
                    if (msgs.isNotEmpty())
                        emiter.onNext(CachedWrapper.CachedData(msgs))
                }
            }

            try {
                // Запрашиваем данные с сервера и возвращаем следом за кэшем
                val apiMessages: List<Message> = messagesZulipApiRequests.getMessages(
                    messageId,
                    numBefore,
                    numAfter,
                    filter.createFilterForMessages()
                ).execute().body()?.messages ?: listOf()
                emiter.onNext(CachedWrapper.OriginalData(apiMessages))

                // обновляем кэш
                if (refreshCache)
                    messagesDao.deleteTopicMessages(filter.streamId, filter.topicName)
                messagesDao.insertAll(apiMessages)

                emiter.onComplete()

            } catch (e: Exception) {
                emiter.onError(e)
            }
        }
    }

    fun getNextPage(messageId: Long, countMessages: Int, filter: NarrowParams): Observable<CachedWrapper<List<Message>>> {
        return getMessages(
            messageId = messageId,
            numBefore = 0,
            numAfter = countMessages,
            filter = filter
        )
    }

    fun getPrevPage(messageId: Long, countMessages: Int, filter: NarrowParams): Observable<CachedWrapper<List<Message>>> {
        return getMessages(
            messageId = messageId,
            numBefore = countMessages,
            numAfter = 0,
            filter = filter
        )
    }

    fun getHistory(messageId: Long, countMessages: Int, filter: NarrowParams): Observable<CachedWrapper<List<Message>>> {
        return getMessages(
            messageId = messageId,
            numBefore = countMessages,
            numAfter = 0,
            filter = filter,
            useCache = true,
            refreshCache = true
        )
    }

    fun sendMessageToStream(
        streamId: Int,
        topic: String,
        message: String
    ): Single<SendResult> {
        return messagesZulipApiRequests.sendMessageToStream(streamId, topic, message)
    }

    fun sendMessageToUsers(usersIds: List<Int>, message: String): Single<SendResult> {
        return messagesZulipApiRequests.sendMessageToUsers(usersIds, message)
    }

    fun addReaction(messageId: Int, emojiName: String): Single<ReactionResult> {
        return messagesZulipApiRequests.addReaction(messageId, emojiName)
    }

    fun removeReaction(messageId: Int, emojiName: String): Single<ReactionResult> {
        return messagesZulipApiRequests.removeReaction(messageId, emojiName)
    }
}