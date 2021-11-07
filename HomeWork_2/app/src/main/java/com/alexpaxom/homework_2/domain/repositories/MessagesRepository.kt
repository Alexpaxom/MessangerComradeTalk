package com.alexpaxom.homework_2.domain.repositories

import com.alexpaxom.homework_2.data.models.MessageItem
import io.reactivex.Single

interface MessagesRepository {
    fun getMessages(): Single<List<MessageItem>>
}