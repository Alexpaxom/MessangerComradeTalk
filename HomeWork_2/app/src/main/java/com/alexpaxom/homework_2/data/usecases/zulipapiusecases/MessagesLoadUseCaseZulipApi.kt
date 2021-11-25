package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.data.modelconverters.MessageConverter
import com.alexpaxom.homework_2.data.models.MessageItem
import com.alexpaxom.homework_2.domain.cache.helpers.CachedWrapper
import com.alexpaxom.homework_2.domain.entity.Message
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.MessagesZulipDataRepository
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.NarrowParams
import io.reactivex.Observable
import java.sql.Wrapper

class MessagesLoadUseCaseZulipApi(
    val ownUserId: Int,
    val messagesZulipDataRepository: MessagesZulipDataRepository = MessagesZulipDataRepository(),
) {
    private val messagesConverter = MessageConverter()

    fun getNextPage(
        messageId: Long,
        countMessages: Int,
        filter: NarrowParams
    ): Observable<CachedWrapper<List<MessageItem>>> {

        return messagesZulipDataRepository.getNextPage(messageId, countMessages, filter)
            .map{ convertTypesMessages(it) }
    }

    fun getPrevPage(
        messageId: Long,
        countMessages: Int, filter:
        NarrowParams
    ): Observable<CachedWrapper<List<MessageItem>>> {

        return messagesZulipDataRepository.getPrevPage(messageId, countMessages, filter)
            .map{ convertTypesMessages(it) }
    }

    fun getHistory(
        messageId: Long,
        countMessages: Int,
        filter: NarrowParams
    ): Observable<CachedWrapper<List<MessageItem>>> {

        return messagesZulipDataRepository.getHistory(messageId, countMessages, filter)
            .map{ convertTypesMessages(it) }

    }

    fun convertTypesMessages(
        messages: CachedWrapper<List<Message>>
    ): CachedWrapper<List<MessageItem>> {
        val convertedMessages =
            messages.data
                    .map { messagesConverter.convert(it) }
                    .map {message ->
                        val typedMessage = if(message.userId == ownUserId)
                            message.copy( typeId = R.layout.my_message_item )
                        else
                            message

                        typedMessage.reactionsGroup.userIdOwner = ownUserId

                        return@map typedMessage
                    }

        return when(messages) {
            is CachedWrapper.CachedData -> CachedWrapper.CachedData(convertedMessages)
            is CachedWrapper.OriginalData -> CachedWrapper.OriginalData(convertedMessages)
        }
    }
}