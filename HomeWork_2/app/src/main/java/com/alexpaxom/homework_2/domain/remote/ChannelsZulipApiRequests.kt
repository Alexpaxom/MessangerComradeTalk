package com.alexpaxom.homework_2.domain.remote

import com.alexpaxom.homework_2.domain.entity.AllStreamsResult
import com.alexpaxom.homework_2.domain.entity.StreamsResult
import com.alexpaxom.homework_2.domain.entity.SubscribeResult
import com.alexpaxom.homework_2.domain.entity.TopicsResult
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChannelsZulipApiRequests {
    @GET("/api/v1/users/me/subscriptions")
    fun getSubscribedStreams(): Call<StreamsResult>

    @GET("/api/v1/streams")
    fun getAllStreams(): Call<AllStreamsResult>

    @GET("/api/v1/users/me/{stream_id}/topics")
    fun getTopicsByStreamId(
        @Path("stream_id") streamId: Int
    ): Call<TopicsResult>

    @POST("/api/v1/users/me/subscriptions")
    fun subscribeOrCreateStream(
        @Query("subscriptions") subscriptions: String,
        @Query("authorization_errors_fatal") authorizationErrorsFatal:Boolean = true
    ): Single<SubscribeResult>
}