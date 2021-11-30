package com.alexpaxom.homework_2.data.usecases

import com.alexpaxom.homework_2.data.models.MessageItem
import io.reactivex.Single

interface MessagesLoadUseCase {
    fun getMessages(): Single<List<MessageItem>>
}