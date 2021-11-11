package com.alexpaxom.homework_2.domain.repositories.zulipapirepositories

import com.alexpaxom.homework_2.data.modelconverters.StatusConverter
import com.alexpaxom.homework_2.data.modelconverters.UserConverter
import com.alexpaxom.homework_2.data.models.UserItem
import com.alexpaxom.homework_2.data.models.UserStatus
import com.alexpaxom.homework_2.domain.remote.UsersZulipApiRequests
import com.alexpaxom.homework_2.domain.repositories.UsersRepository
import io.reactivex.Single

class UsersZulipDateRepositoryImpl: UsersRepository {

    private val retrofit = GetRetrofitObject.retrofit
    private val usersZulipApiRequests = retrofit.create(UsersZulipApiRequests::class.java)
    private val userConverter = UserConverter()
    private val statusConverter = StatusConverter()

    override fun getUsers(): Single<List<UserItem>> {
        return usersZulipApiRequests.getUsers()
            .map { usersWrapper ->
                usersWrapper
                    .members
                    .filter { it.isActive && !it.isBot}
                    .map {
                    userConverter.convert(it)
                }
            }
    }

    override fun getUserById(userId: Int?): Single<UserItem> {
        return if(userId != null)
            usersZulipApiRequests.getUserById(userId).map{ userConverter.convert(it.user) }
        else
            usersZulipApiRequests.getOwnUser().map{ userConverter.convert(it) }
    }


    override fun getUserPresence(userId: Int): Single<UserStatus> {
        return usersZulipApiRequests.getUserPresence(userId). map { userStatus->
            statusConverter.convert(userStatus, userId)
        }
    }
}