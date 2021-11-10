package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.data.models.UserItem
import com.alexpaxom.homework_2.data.usecases.UserProfileUseCase
import com.alexpaxom.homework_2.domain.repositories.UsersRepository
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.UsersZulipDateRepositoryImpl
import io.reactivex.Single

class UserProfileUseCaseZulipApiImpl (
    private val usersRepository: UsersRepository = UsersZulipDateRepositoryImpl()
): UserProfileUseCase {
    override fun getUserByID(userId: Int?): Single<UserItem> {
        return usersRepository.getUserById(userId)
    }
}