package com.alexpaxom.homework_2.data.usecases

import com.alexpaxom.homework_2.domain.entity.SendResult
import io.reactivex.Single

interface MessageSendUseCase {
    fun sendMessageToStream(
        streamId: Int,
        topic: String,
        message: String,
    ): Single<SendResult>

    fun sendMessageToUsers(
        usersIds: List<Int>,
        message: String,
    ): Single<SendResult>
}