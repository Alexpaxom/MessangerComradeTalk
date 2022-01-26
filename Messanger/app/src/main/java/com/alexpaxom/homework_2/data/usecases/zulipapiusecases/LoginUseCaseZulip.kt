package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.di.screen.ScreenScope
import com.alexpaxom.homework_2.domain.cache.helpers.CachedWrapper
import com.alexpaxom.homework_2.domain.entity.LoginResult
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.LoginZulipRepository
import io.reactivex.Single
import javax.inject.Inject

@ScreenScope
class LoginUseCaseZulip @Inject constructor(
    private val loginZulipRepository: LoginZulipRepository
) {

    fun logIn(
        username: String? = null,
        password: String? = null,
        organizationPath: String? = null,
        saveUserParams: Boolean = true,
        forceApi: Boolean = false,
        onlyCached : Boolean = false
    ): Single<CachedWrapper<LoginResult>> {
        return loginZulipRepository.login(
            username = username,
            password = password,
            organizationPath = organizationPath,
            saveUserParams = saveUserParams,
            forceApi = forceApi,
            onlyCached =onlyCached
        )
    }
}