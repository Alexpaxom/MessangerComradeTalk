package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.data.models.MessageItem
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.MessagesZulipDataRepository
import io.reactivex.Single

class MessagesLoadUseCaseZulipApi(
    val ownUserId: Int,
    val messagesZulipDataRepository: MessagesZulipDataRepository = MessagesZulipDataRepository(),
) {
    fun getMessages(
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
}