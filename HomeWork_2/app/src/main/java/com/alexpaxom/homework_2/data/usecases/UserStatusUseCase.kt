package com.alexpaxom.homework_2.data.usecases

import com.alexpaxom.homework_2.data.models.UserStatus
import io.reactivex.Single

interface UserStatusUseCase {
    fun getStatusForUser(userId: Int): Single<UserStatus>
}