package com.alexpaxom.homework_2.data.usecases.testusecases

import com.alexpaxom.homework_2.data.models.UserItem
import com.alexpaxom.homework_2.data.usecases.UserProfileUseCase
import com.alexpaxom.homework_2.domain.repositories.testrepositories.UsersTestDataRepositoryImpl
import io.reactivex.Single

class UserProfileUseCaseTestImpl (
    val usersRepository: UsersTestDataRepositoryImpl = UsersTestDataRepositoryImpl()
): UserProfileUseCase {
    override fun getUserByID(userId: Int): Single<UserItem> {
        return usersRepository.getUserById(userId)
    }
}