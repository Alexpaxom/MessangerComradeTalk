package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.data.models.MessageItem
import com.alexpaxom.homework_2.data.usecases.MessagesLoadUseCase
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.MessagesZulipDataRepositoryImpl
import io.reactivex.Single

class MessagesLoadUseCaseZulipApiImpl(
    val messagesZulipDataRepository: MessagesZulipDataRepositoryImpl = MessagesZulipDataRepositoryImpl()
): MessagesLoadUseCase {
    override fun getMessages(): Single<List<MessageItem>> {
        return messagesZulipDataRepository.getMessages()
    }
}