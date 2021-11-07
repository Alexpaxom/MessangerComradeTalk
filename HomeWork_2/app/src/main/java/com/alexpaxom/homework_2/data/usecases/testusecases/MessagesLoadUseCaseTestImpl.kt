package com.alexpaxom.homework_2.data.usecases.testusecases

import com.alexpaxom.homework_2.data.models.MessageItem
import com.alexpaxom.homework_2.data.usecases.MessagesLoadUseCase
import com.alexpaxom.homework_2.domain.repositories.testrepositories.MessagesTestDataRepository
import io.reactivex.Single

class MessagesLoadUseCaseTestImpl (
    val messagesTestDataRepository: MessagesTestDataRepository = MessagesTestDataRepository()
): MessagesLoadUseCase {
    override fun getMessages(): Single<List<MessageItem>> {
        return messagesTestDataRepository.getMessages()
    }
}