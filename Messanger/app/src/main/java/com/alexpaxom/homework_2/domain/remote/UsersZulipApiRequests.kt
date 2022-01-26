package com.alexpaxom.homework_2.domain.remote

import com.alexpaxom.homework_2.domain.entity.User
import com.alexpaxom.homework_2.domain.entity.UserPresence
import com.alexpaxom.homework_2.domain.entity.UserResult
import com.alexpaxom.homework_2.domain.entity.UsersResult
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UsersZulipApiRequests {
    @GET("/api/v1/users")
    fun getUsers(): Call<UsersResult>

    @GET("/api/v1/users/{user_id}")
    fun getUserById(
        @Path("user_id") userId: Int
    ): Call<UserResult>

    @GET("/api/v1/users/me")
    fun getOwnUser(): Call<User>

    @GET("/api/v1/users/{user_id}/presence")
    fun getUserPresence(
        @Path("user_id") userId: Int
    ): Single<UserPresence>
}