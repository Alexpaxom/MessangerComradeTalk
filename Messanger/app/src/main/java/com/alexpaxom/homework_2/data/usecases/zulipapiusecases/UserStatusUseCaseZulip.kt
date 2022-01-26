package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.data.modelconverters.StatusConverter
import com.alexpaxom.homework_2.data.models.UserStatus
import com.alexpaxom.homework_2.di.screen.ScreenScope
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.UsersZulipDateRepository
import io.reactivex.Single
import javax.inject.Inject

@ScreenScope
class UserStatusUseCaseZulip @Inject constructor(
    private val usersRepository: UsersZulipDateRepository
) {

    private val statusConverter = StatusConverter()

    fun getStatusForUser(userId: Int): Single<UserStatus> {
        return usersRepository
                .getUserPresence(userId)
                .map { statusConverter.convert(it, userId) }
    }
}