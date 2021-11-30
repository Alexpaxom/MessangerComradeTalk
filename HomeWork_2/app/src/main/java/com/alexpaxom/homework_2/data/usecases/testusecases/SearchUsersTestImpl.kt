package com.alexpaxom.homework_2.data.usecases.testusecases

import com.alexpaxom.homework_2.data.models.UserItem
import com.alexpaxom.homework_2.data.usecases.SearchUsers
import com.alexpaxom.homework_2.domain.repositories.UsersRepository
import com.alexpaxom.homework_2.domain.repositories.testrepositories.UsersTestDataRepositoryImpl
import io.reactivex.Single

class SearchUsersTestImpl(
    val usersRepository: UsersRepository = UsersTestDataRepositoryImpl()
): SearchUsers {
    override fun search(searchString: String): Single<List<UserItem>> {
        return usersRepository.getUsers().map { users ->
            users.filter { user->
                user.name.contains(searchString, ignoreCase = true) ||
                        user.email.contains(searchString, ignoreCase = true)
            }
        }
    }
}