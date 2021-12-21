package com.alexpaxom.homework_2.domain.remote

import com.alexpaxom.homework_2.domain.entity.LoginResult
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LoginZulipApiRequests {
    @POST("https://{organization_path}/api/v1/fetch_api_key")
    fun login(
        @Path("organization_path") organizationPath: String,
        @Query("username") username: String,
        @Query("password") password: String,
    ): Call<LoginResult>
}