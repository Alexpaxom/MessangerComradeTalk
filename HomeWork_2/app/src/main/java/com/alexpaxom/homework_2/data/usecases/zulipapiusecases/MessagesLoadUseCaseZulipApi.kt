package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.data.modelconverters.MessageConverter
import com.alexpaxom.homework_2.data.models.MessageItem
import com.alexpaxom.homework_2.domain.entity.Message
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.MessagesZulipDataRepository
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.NarrowParams
import io.reactivex.Observable

class MessagesLoadUseCaseZulipApi(
    val ownUserId: Int,
    val messagesZulipDataRepository: MessagesZulipDataRepository = MessagesZulipDataRepository(),
) {
    private val messagesConverter = MessageConverter()

    fun getNextPage(messageId: Long, countMessages: Int, filter: NarrowParams): Observable<List<MessageItem>> {
        return convertTypesMessages(
            messagesZulipDataRepository.getNextPage(messageId, countMessages, filter)
        )
    }

    fun getPrevPage(messageId: Long, countMessages: Int, filter: NarrowParams): Observable<List<MessageItem>> {
        return convertTypesMessages(
            messagesZulipDataRepository.getPrevPage(messageId, countMessages, filter)
        )
    }

    fun getHistory(messageId: Long, countMessages: Int, filter: NarrowParams): Observable<List<MessageItem>> {
        return convertTypesMessages(
            messagesZulipDataRepository.getHistory(messageId, countMessages, filter)
        )
    }

    fun convertTypesMessages(
        messages: Observable<List<Message>>
    ): Observable<List<MessageItem>> {
        return messages
            .map {
                it
                    .map { messagesConverter.convert(it) }
                    .map {message ->
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