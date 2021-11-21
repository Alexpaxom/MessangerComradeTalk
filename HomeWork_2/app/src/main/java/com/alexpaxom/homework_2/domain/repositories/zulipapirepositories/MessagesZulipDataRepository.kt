package com.alexpaxom.homework_2.domain.repositories.zulipapirepositories

import com.alexpaxom.homework_2.data.modelconverters.MessageConverter
import com.alexpaxom.homework_2.data.models.MessageItem
import com.alexpaxom.homework_2.domain.entity.ReactionResult
import com.alexpaxom.homework_2.domain.entity.SendResult
import com.alexpaxom.homework_2.domain.remote.MessagesZulipApiRequests
import io.reactivex.Single

class MessagesZulipDataRepository {

    private val retrofit = GetRetrofitObject.retrofit
    private val messagesZulipApiRequests = retrofit.create(MessagesZulipApiRequests::class.java)
    private val messagesConverter = MessageConverter()

    fun getMessages(
        messageId: Long,
        numBefore:Int,
        numAfter:Int,
        filter:String?,
    ): Single<List<MessageItem>> {
        return messagesZulipApiRequests
            .getMessages( messageId, numBefore, numAfter, filter)
            .map {
            it.messages.map{ message -> messagesConverter.convert(message) }
        }
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