package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.data.usecases.MessageSendUseCase
import com.alexpaxom.homework_2.domain.entity.ReactionResult
import com.alexpaxom.homework_2.domain.entity.SendResult
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.MessagesZulipDataRepositoryImpl
import io.reactivex.Single

class MessageSendUseCaseZulipApiImpl (
    val messagesZulipDataRepository: MessagesZulipDataRepositoryImpl = MessagesZulipDataRepositoryImpl()
): MessageSendUseCase {
    override fun sendMessageToStream(
        streamId: Int,
        topic: String,
        message: String
    ): Single<SendResult> {
        return messagesZulipDataRepository.sendMessageToStream(streamId, topic, message)
    }

    override fun sendMessageToUsers(usersIds: List<Int>, message: String): Single<SendResult> {
        return messagesZulipDataRepository.sendMessageToUsers(usersIds, message)
    }

    override fun addReaction(messageId: Int, emojiName: String): Single<ReactionResult> {
        return messagesZulipDataRepository.addReaction(messageId, emojiName)
    }

    override fun removeReaction(messageId: Int, emojiName: String): Single<ReactionResult> {
        return messagesZulipDataRepository.removeReaction(messageId, emojiName)
    }
}