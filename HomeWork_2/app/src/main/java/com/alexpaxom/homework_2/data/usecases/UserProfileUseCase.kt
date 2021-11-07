package com.alexpaxom.homework_2.data.usecases

import com.alexpaxom.homework_2.data.models.UserItem
import io.reactivex.Single

interface UserProfileUseCase {
    fun getUserByID(userId: Int): Single<UserItem>
}