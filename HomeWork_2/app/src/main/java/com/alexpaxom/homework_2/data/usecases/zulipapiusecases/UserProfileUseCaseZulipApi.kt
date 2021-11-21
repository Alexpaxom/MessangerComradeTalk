package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.data.models.UserItem
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.UsersZulipDateRepository
import io.reactivex.Single

class UserProfileUseCaseZulipApi (
    private val usersRepository: UsersZulipDateRepository = UsersZulipDateRepository()
) {
    fun getUserByID(userId: Int?): Single<UserItem> {
        return usersRepository.getUserById(userId)
    }
}