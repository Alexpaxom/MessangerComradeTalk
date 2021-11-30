package com.alexpaxom.homework_2.domain.repositories

import com.alexpaxom.homework_2.data.models.MessageItem
import com.alexpaxom.homework_2.domain.entity.ReactionResult
import com.alexpaxom.homework_2.domain.entity.SendResult
import io.reactivex.Single

interface MessagesRepository {
    fun getMessages(
        messageId: Long,
        numBefore:Int,
        numAfter:Int,
        filter:String?,
    ): Single<List<MessageItem>>

    fun sendMessageToStream(
        streamId: Int,
        topic: String,
        message: String,
    ): Single<SendResult>

    fun sendMessageToUsers(
        usersIds: List<Int>,
        message: String,
    ): Single<SendResult>

    fun addReaction(
        messageId: Int,
        emojiName: String,
    ): Single<ReactionResult>

    fun removeReaction(
        messageId: Int,
        emojiName: String,
    ): Single<ReactionResult>
}