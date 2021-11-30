package com.alexpaxom.homework_2.domain.repositories.zulipapirepositories

import com.alexpaxom.homework_2.di.screen.ScreenScope
import com.alexpaxom.homework_2.domain.cache.daos.UsersDAO
import com.alexpaxom.homework_2.domain.cache.helpers.CachedWrapper
import com.alexpaxom.homework_2.domain.entity.User
import com.alexpaxom.homework_2.domain.entity.UserPresence
import com.alexpaxom.homework_2.domain.remote.UsersZulipApiRequests
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

@ScreenScope
class UsersZulipDateRepository @Inject constructor(
    private val usersZulipApiRequests: UsersZulipApiRequests,
    private val usersDAO: UsersDAO
) {

    fun getUsers(
        useCache: Boolean = true,
        refreshCache: Boolean = true,
    ): Observable<CachedWrapper<List<User>>> {
        return Observable.create { emitter->
            // Возвращаем кэш если если он есть
            if(useCache) {
                val users = usersDAO.getAll()
                if(users.isNotEmpty())
                    emitter.onNext(CachedWrapper.CachedData(users))
            }

            try {
                // Запрашиваем данные с сервера и возвращаем следом за кэшем
                val users = usersZulipApiRequests.getUsers().execute().body()?.members ?: listOf()
                emitter.onNext(CachedWrapper.OriginalData(users))

                // обновляем кэш
                if(refreshCache)
                    usersDAO.deleteAllUsers()
                usersDAO.insertAll(users)

                // TODO пока обновляем каждый раз обновляем запись с собственных пользователем
                // но потом это нужно будет перенести в отдельную таблицу либо в shared pref
                usersDAO.insertAll(
                    listOf(
                        (usersZulipApiRequests.getOwnUser().execute().body() ?: defaultUser)
                            .copy(isMyUser = true)
                    )
                )
            }
            catch (e: Exception) {
                emitter.tryOnError(e)
            }

            emitter.onComplete()
        }
    }

    fun getUserById(
        userId: Int? = null,
        useCache: Boolean = true,
        refreshCache: Boolean = true,
    ): Observable<CachedWrapper<User>> {
        return Observable.create { emitter->
            // Возвращаем кэш если если он есть
            if(useCache) {
                val user = if(userId != null)
                    usersDAO.getUserById(userId)
                else
                    usersDAO.getOwnUser()

                if(user != null)
                    emitter.onNext(CachedWrapper.CachedData(user))
            }

            try {
                // Запрашиваем данные с сервера и возвращаем следом за кэшем
                val user = if(userId != null)
                    usersZulipApiRequests.getUserById(userId).execute().body()?.user ?: defaultUser
                else
                    (usersZulipApiRequests.getOwnUser().execute().body() ?: defaultUser)
                        .copy(isMyUser = true)


                emitter.onNext(CachedWrapper.OriginalData(user))

                // обновляем кэш
                if(refreshCache)
                    usersDAO.insertAll(listOf(user))

                emitter.onComplete()
            }
            catch (e: Exception) {
                emitter.tryOnError(e)
            }
        }
    }


    fun getUserPresence(userId: Int): Single<UserPresence> {
        return usersZulipApiRequests.getUserPresence(userId)
    }

    private val defaultUser = User(
        userId = 0,
        avatarUrl = "emptyUserAvatar",
        email = "emptyUserEmail",
        fullName = "Empty User",
        isActive = false,
        isBot = false
    )
}