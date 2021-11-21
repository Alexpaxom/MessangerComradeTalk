package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.data.models.UserStatus
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.UsersZulipDateRepository
import io.reactivex.Single

class UserStatusUseCaseZulipApi(
    private val usersRepository: UsersZulipDateRepository = UsersZulipDateRepository()
) {
    fun getStatusForUser(userId: Int): Single<UserStatus> {
        return usersRepository.getUserPresence(userId)
    }
}