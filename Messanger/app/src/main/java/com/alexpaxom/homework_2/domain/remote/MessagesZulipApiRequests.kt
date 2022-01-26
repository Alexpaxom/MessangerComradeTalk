package com.alexpaxom.homework_2.domain.remote

import com.alexpaxom.homework_2.domain.entity.MessageUpdateResult
import com.alexpaxom.homework_2.domain.entity.MessagesResult
import com.alexpaxom.homework_2.domain.entity.ReactionResult
import com.alexpaxom.homework_2.domain.entity.SendResult
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface MessagesZulipApiRequests {
    @GET("/api/v1/messages")
    fun getMessages(
        @Query("anchor") anchorId: Long,
        @Query("num_before") numBefore:Int,
        @Query("num_after") numAfter:Int,
        @Query("narrow") filter:String?,
    ): Call<MessagesResult>

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

    @POST("/api/v1/messages/{messageId}/reactions")
    fun addReaction(
        @Path("messageId") messageId: Int,
        @Query("emoji_name") emojiName: String,
    ): Single<ReactionResult>

    @DELETE("/api/v1/messages/{messageId}/reactions")
    fun removeReaction(
        @Path("messageId") messageId: Int,
        @Query("emoji_name") emojiName: String,
    ): Single<ReactionResult>

    @DELETE("/api/v1/messages/{messageId}")
    fun removeMessage(
        @Path("messageId") messageId: Int,
    ): Single<MessageUpdateResult>

    @PATCH("/api/v1/messages/{messageId}")
    fun editMessage(
        @Path("messageId") messageId: Int,
        @QueryMap hashMap: Map<String, String>
    ): Single<MessageUpdateResult>
}