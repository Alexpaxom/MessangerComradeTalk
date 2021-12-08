package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.data.modelconverters.StatusConverter
import com.alexpaxom.homework_2.data.models.UserStatus
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.UsersZulipDateRepository
import io.reactivex.Single

class UserStatusUseCaseZulip(
    private val usersRepository: UsersZulipDateRepository = UsersZulipDateRepository()
) {

    private val statusConverter = StatusConverter()

    fun getStatusForUser(userId: Int): Single<UserStatus> {
        return usersRepository
                .getUserPresence(userId)
                .map { statusConverter.convert(it, userId) }
    }
}