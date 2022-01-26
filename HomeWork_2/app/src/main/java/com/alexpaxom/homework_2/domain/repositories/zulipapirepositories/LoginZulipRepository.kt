package com.alexpaxom.homework_2.domain.repositories.zulipapirepositories

import com.alexpaxom.homework_2.di.screen.ScreenScope
import com.alexpaxom.homework_2.domain.cache.daos.LoginDAO
import com.alexpaxom.homework_2.domain.cache.helpers.CachedWrapper
import com.alexpaxom.homework_2.domain.entity.LoginResult
import com.alexpaxom.homework_2.domain.remote.LoginZulipApiRequests
import io.reactivex.Single
import javax.inject.Inject

@ScreenScope
class LoginZulipRepository @Inject constructor(
    private val loginZulipApiRequests: LoginZulipApiRequests,
    private val loginDao: LoginDAO
) {
    fun login(
        username: String? = null,
        password: String? = null,
        organizationPath: String? = null,
        saveUserParams: Boolean = true,
        forceApi: Boolean = false,
        onlyCached : Boolean = false
    ): Single<CachedWrapper<LoginResult>> {

        return Single.create { emitter ->
            // Возвращаем кэш если если он есть
            var loginUserParams: CachedWrapper<LoginResult>? = null

            if (!forceApi) {
                val localLoginUserParams = loginDao.getLoginUserParams()
                loginUserParams = if (localLoginUserParams.isNotEmpty())
                    CachedWrapper.CachedData(localLoginUserParams.first())
                else null
            }

            if ((forceApi || loginUserParams == null) && !onlyCached) {
                try {
                    requireNotNull(username) {"Need login"}
                    requireNotNull(password) {"Need password"}
                    requireNotNull(organizationPath) {"Need organizationPath"}

                    val apiLoginUserParams = loginZulipApiRequests.login(
                        organizationPath = organizationPath.replace(
                            oldValue = URL_PREFIX,
                            newValue = "",
                            ignoreCase = true
                        ),
                        username = username,
                        password = password
                    )
                        .execute()
                        .body()
                        ?.copy(url = organizationPath) ?: error("Bad login params")

                    loginUserParams = CachedWrapper.OriginalData(apiLoginUserParams)

                    // обновляем кэш
                    loginDao.deleteAllLoginParams()
                    if (saveUserParams)
                        loginDao.insert(apiLoginUserParams)
                } catch (e: Exception) {
                    emitter.tryOnError(e)
                }
            }

            try {
                emitter.onSuccess(loginUserParams ?: error("Can't get login params"))
            }
            catch (e: Exception) {
                emitter.tryOnError(e)
            }

        }
    }

    companion object {
        private const val URL_PREFIX = "https://"
    }
}