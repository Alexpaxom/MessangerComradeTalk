package com.alexpaxom.homework_2.domain.remote

import com.alexpaxom.homework_2.domain.entity.MessagesResult
import com.alexpaxom.homework_2.domain.entity.SendResult
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MessagesZulipApiRequests {
    @GET("/api/v1/messages")
    fun getMessages(
        @Query("anchor") anchorId: Long,
        @Query("num_before") numBefore:Int,
        @Query("num_after") numAfter:Int,
        @Query("narrow") filter:String?,
    ): Single<MessagesResult>

    @POST("/api/v1/messages")
    fun sendMessageToStream(
        @Query("to") streamId: Int,
        @Query("topic") topic: String,
        @Query("content") message: String,
        @Query("type") type: String = "stream"
    ): Single<SendResult>

    @POST("/api/v1/messages")
    fun sendMessageToUsers(
        @Query("to") usersIds: List<Int>,
        @Query("content") message: String,
        @Query("type") type: String = "private"
    ): Single<SendResult>
}