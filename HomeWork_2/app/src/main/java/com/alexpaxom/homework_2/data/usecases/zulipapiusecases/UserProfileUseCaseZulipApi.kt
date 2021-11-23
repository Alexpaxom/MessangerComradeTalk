package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.data.modelconverters.UserConverter
import com.alexpaxom.homework_2.data.models.UserItem
import com.alexpaxom.homework_2.domain.cache.helpers.CachedWrapper
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.UsersZulipDateRepository
import io.reactivex.Observable

class UserProfileUseCaseZulipApi (
    private val usersRepository: UsersZulipDateRepository = UsersZulipDateRepository()
) {
    private val userConverter = UserConverter()

    fun getUserByID(userId: Int? = null): Observable<CachedWrapper<UserItem>> {
        return usersRepository
                 .getUserById(userId)
                 .map { userWrap ->
                     val user = userConverter.convert(userWrap.data)

                     when(userWrap) {
                         is CachedWrapper.CachedData -> CachedWrapper.CachedData(user)
                         is CachedWrapper.OriginalData -> CachedWrapper.OriginalData(user)
                         is CachedWrapper.ErrorResult -> CachedWrapper.ErrorResult(user, userWrap.error)
                     }
                 }
    }
}