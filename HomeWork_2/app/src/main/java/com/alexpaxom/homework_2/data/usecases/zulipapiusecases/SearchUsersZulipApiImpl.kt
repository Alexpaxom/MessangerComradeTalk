package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import android.util.Log
import com.alexpaxom.homework_2.data.models.UserItem
import com.alexpaxom.homework_2.data.usecases.SearchUsers
import com.alexpaxom.homework_2.domain.repositories.UsersRepository
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.UsersZulipDateRepositoryImpl
import io.reactivex.Single

class SearchUsersZulipApiImpl (
    val usersRepository: UsersRepository = UsersZulipDateRepositoryImpl()
): SearchUsers {
    override fun search(searchString: String): Single<List<UserItem>> {
        Log.e("TEST", "search: ${Thread.currentThread().name}")
        return usersRepository.getUsers().map { users ->
            users.filter { user->
                user.name.contains(searchString, ignoreCase = true) ||
                        user.email.contains(searchString, ignoreCase = true)
            }
        }
    }
}