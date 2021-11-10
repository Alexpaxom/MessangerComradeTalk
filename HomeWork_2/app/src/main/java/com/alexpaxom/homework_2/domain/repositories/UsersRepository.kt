package com.alexpaxom.homework_2.domain.repositories

import com.alexpaxom.homework_2.data.models.UserItem
import com.alexpaxom.homework_2.domain.entity.UserPresence
import io.reactivex.Single

interface UsersRepository {
    fun getUsers() : Single<List<UserItem>>
    // if null return own user info
    fun getUserById(userId: Int? = null) : Single<UserItem>
    fun getUserPresence(userId: Int) : Single<UserPresence>
}