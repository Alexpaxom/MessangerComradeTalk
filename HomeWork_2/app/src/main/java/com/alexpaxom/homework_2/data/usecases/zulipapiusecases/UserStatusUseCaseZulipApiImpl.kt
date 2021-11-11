package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.data.models.UserStatus
import com.alexpaxom.homework_2.data.usecases.UserStatusUseCase
import com.alexpaxom.homework_2.domain.repositories.UsersRepository
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.UsersZulipDateRepositoryImpl
import io.reactivex.Single

class UserStatusUseCaseZulipApiImpl(
    private val usersRepository: UsersRepository = UsersZulipDateRepositoryImpl()
): UserStatusUseCase {
    override fun getStatusForUser(userId: Int): Single<UserStatus> {
        return usersRepository.getUserPresence(userId)
    }
}