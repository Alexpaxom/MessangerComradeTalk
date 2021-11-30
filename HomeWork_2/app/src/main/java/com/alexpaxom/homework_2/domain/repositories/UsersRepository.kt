package com.alexpaxom.homework_2.domain.repositories

import com.alexpaxom.homework_2.data.models.UserItem
import io.reactivex.Single

interface UsersRepository {
    fun getUsers() : Single<List<UserItem>>
    fun getUserById(userId: Int) : Single<UserItem>
}