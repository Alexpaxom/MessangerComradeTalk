package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.data.models.UserItem
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.UsersZulipDateRepository
import io.reactivex.Single

class SearchUsersZulipApi (
    val usersRepository: UsersZulipDateRepository = UsersZulipDateRepository()
) {
    fun search(searchString: String): Single<List<UserItem>> {
        return usersRepository.getUsers().map { users ->
            users.filter { user->
                user.name.contains(searchString, ignoreCase = true) ||
                        user.email.contains(searchString, ignoreCase = true)
            }
        }
    }
}