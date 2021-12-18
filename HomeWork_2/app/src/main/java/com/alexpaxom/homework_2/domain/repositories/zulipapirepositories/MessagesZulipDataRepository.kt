package com.alexpaxom.homework_2.domain.repositories.zulipapirepositories

import android.util.Log
import com.alexpaxom.homework_2.di.screen.ScreenScope
import com.alexpaxom.homework_2.domain.cache.daos.MessagesDAO
import com.alexpaxom.homework_2.domain.cache.helpers.CachedWrapper
import com.alexpaxom.homework_2.domain.entity.Message
import com.alexpaxom.homework_2.domain.entity.MessageUpdateResult
import com.alexpaxom.homework_2.domain.entity.ReactionResult
import com.alexpaxom.homework_2.domain.entity.SendResult
import com.alexpaxom.homework_2.domain.remote.MessagesZulipApiRequests
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

@ScreenScope
class MessagesZulipDataRepository @Inject constructor(
    private val messagesZulipApiRequests: MessagesZulipApiRequests,
    private val messagesDao: MessagesDAO
) {

    private fun getMessages(
        messageId: Long,
        numBefore: Int,
        numAfter: Int,
        filter: NarrowParams,
        useCache: Boolean = false,
        refreshCache: Boolean = false,
    ): Observable<CachedWrapper<List<Message>>> {
        return Observable.create { emiter ->
            // Возвращаем кэш если если он есть
            if (useCache) {
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

    fun getNextPage(
        messageId: Long,
        countMessages: Int,
        filter: NarrowParams
    ): Observable<CachedWrapper<List<Message>>> {
        return getMessages(
            messageId = messageId,
            numBefore = 0,
            numAfter = countMessages,
            filter = filter
        )
    }

    fun getPrevPage(
        messageId: Long,
        countMessages: Int,
        filter: NarrowParams
    ): Observable<CachedWrapper<List<Message>>> {
        return getMessages(
            messageId = messageId,
            numBefore = countMessages,
            numAfter = 0,
            filter = filter
        )
    }

    fun getHistory(
        messageId: Long, countMessages: Int,
        filter: NarrowParams,
        useCache: Boolean = true,
        refreshCache: Boolean = true
    ): Observable<CachedWrapper<List<Message>>> {
        return getMessages(
            messageId = messageId,
            numBefore = countMessages,
            numAfter = 0,
            filter = filter,
            useCache = useCache,
            refreshCache = refreshCache
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

    fun removeMessage(messageId: Int): Single<MessageUpdateResult> {
        return messagesZulipApiRequests.removeMessage(messageId)
    }

    fun editMessage(
        messageId: Int,
        channelId: Int? = null,
        topic: String? = null,
        message: String? = null
    ): Single<MessageUpdateResult> {

        val params = HashMap<String, String>()
        channelId?.let { params.put(MESSAGE_EDIT_STREAM_ID_PARAM_NAME, it.toString()) }
        topic?.let { params.put(MESSAGE_EDIT_TOPIC_PARAM_NAME, it) }
        message?.let { params.put(MESSAGE_EDIT_CONTENT_PARAM_NAME, it) }

        return messagesZulipApiRequests.editMessage(messageId, params)
    }

    fun addReaction(messageId: Int, emojiName: String): Single<ReactionResult> {
        return messagesZulipApiRequests.addReaction(messageId, emojiName)
    }

    fun removeReaction(messageId: Int, emojiName: String): Single<ReactionResult> {
        return messagesZulipApiRequests.removeReaction(messageId, emojiName)
    }

    companion object {
        private const val MESSAGE_EDIT_STREAM_ID_PARAM_NAME = "stream_id"
        private const val MESSAGE_EDIT_TOPIC_PARAM_NAME = "topic"
        private const val MESSAGE_EDIT_CONTENT_PARAM_NAME = "content"
    }
}