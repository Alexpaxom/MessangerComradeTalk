package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.data.models.MessageItem
import com.alexpaxom.homework_2.data.usecases.MessagesLoadUseCase
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.MessagesZulipDataRepositoryImpl
import io.reactivex.Single

class MessagesLoadUseCaseZulipApiImpl(
    val ownUserId: Int,
    val messagesZulipDataRepository: MessagesZulipDataRepositoryImpl = MessagesZulipDataRepositoryImpl(),
): MessagesLoadUseCase {
    override fun getMessages(
        messageId: Long,
        numBefore:Int,
        numAfter:Int,
        filter:String?,
    ): Single<List<MessageItem>> {
        return messagesZulipDataRepository.getMessages( messageId, numBefore, numAfter, filter )
            .map {
                it.map {message ->

                    val typedMessage = if(message.userId == ownUserId)
                        message.copy( typeId = R.layout.my_message_item )
                    else
                        message

                    typedMessage.reactionsGroup.userIdOwner = ownUserId

                    return@map typedMessage
                }
            }
    }

    companion object {
        // get from documentation api
        const val NEWEST_MESSAGE = 10000000000000000L
        const val OLDEST_MESSAGE = 0L
    }
}