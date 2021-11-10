package com.alexpaxom.homework_2.data.usecases

import com.alexpaxom.homework_2.data.models.UserItem
import io.reactivex.Single

interface UserProfileUseCase {
    // if null return own user
    fun getUserByID(userId: Int? = null): Single<UserItem>
}