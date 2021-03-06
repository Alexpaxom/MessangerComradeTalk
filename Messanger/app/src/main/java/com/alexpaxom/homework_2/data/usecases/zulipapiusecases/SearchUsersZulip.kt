package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.data.modelconverters.UserConverter
import com.alexpaxom.homework_2.data.models.UserItem
import com.alexpaxom.homework_2.di.screen.ScreenScope
import com.alexpaxom.homework_2.domain.cache.helpers.CachedWrapper
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.UsersZulipDateRepository
import io.reactivex.Observable
import javax.inject.Inject

@ScreenScope
class SearchUsersZulip @Inject constructor(
    private val usersRepository: UsersZulipDateRepository,
) {
    private val userConverter = UserConverter()

    fun search(searchString: String): Observable<CachedWrapper<List<UserItem>>> {
        return usersRepository.getUsers()
            .map { usersWrap->

                val users = usersWrap.data.filter { it.isActive ?: false && !(it.isBot ?: true) }
                    .map{userConverter.convert(it)}

                when(usersWrap) {
                    is CachedWrapper.CachedData ->
                        CachedWrapper.CachedData(searchUsers(users, searchString))
                    is CachedWrapper.OriginalData ->
                        CachedWrapper.OriginalData(searchUsers(users, searchString))
                }
            }

        }

        private fun searchUsers(users: List<UserItem>, searchString: String): List<UserItem> {
            return users.filter { user->
                        user.name.contains(searchString, ignoreCase = true) ||
                                user.email.contains(searchString, ignoreCase = true)
        }
    }
}