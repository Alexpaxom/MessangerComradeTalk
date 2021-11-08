package com.alexpaxom.homework_2.domain.repositories.zulipapirepositories

import com.alexpaxom.homework_2.data.modelconverters.MessagesConverter
import com.alexpaxom.homework_2.data.models.MessageItem
import com.alexpaxom.homework_2.domain.remote.MessagesZulipApiRequests
import com.alexpaxom.homework_2.domain.repositories.MessagesRepository
import io.reactivex.Single

class MessagesZulipDataRepositoryImpl: MessagesRepository {


    private val retrofit = GetRetrofitObject.retrofit

    private val MessagesZulipApiRequests = retrofit.create(MessagesZulipApiRequests::class.java)

    private val messagesConverter = MessagesConverter()


    override fun getMessages(): Single<List<MessageItem>> {
        return MessagesZulipApiRequests.getMessages().map {
            it.messages.map{ message -> messagesConverter.convert(message) }
        }
    }
}