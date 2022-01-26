package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.di.screen.ScreenScope
import com.alexpaxom.homework_2.domain.entity.MessageUpdateResult
import com.alexpaxom.homework_2.domain.entity.ReactionResult
import com.alexpaxom.homework_2.domain.entity.SendResult
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.MessagesZulipDataRepository
import io.reactivex.Single
import javax.inject.Inject

@ScreenScope
class EditMessagesUseCaseZulip @Inject constructor(
    private val messagesZulipDataRepository: MessagesZulipDataRepository
) {
    fun sendMessageToStream(
        streamId: Int,
        topic: String,
        message: String
    ): Single<SendResult> {
        return messagesZulipDataRepository.sendMessageToStream(streamId, topic, message)
    }

    fun sendMessageToUsers(usersIds: List<Int>, message: String): Single<SendResult> {
        return messagesZulipDataRepository.sendMessageToUsers(usersIds, message)
    }

    fun removeMessage(messageId: Int): Single<MessageUpdateResult> {
        return messagesZulipDataRepository.removeMessage(messageId)
    }

    fun editMessage(
        messageId: Int,
        channelId: Int? = null,
        topic: String? = null,
        message: String? = null
    ): Single<MessageUpdateResult> {
        
        return messagesZulipDataRepository.editMessage(
            messageId = messageId,
            channelId = channelId,
            topic = topic,
            message= message
        )
    }

    fun addReaction(messageId: Int, emojiName: String): Single<ReactionResult> {
        return messagesZulipDataRepository.addReaction(messageId, emojiName)
    }

    fun removeReaction(messageId: Int, emojiName: String): Single<ReactionResult> {
        return messagesZulipDataRepository.removeReaction(messageId, emojiName)
    }
}