package com.alexpaxom.homework_2.domain.remote

import com.alexpaxom.homework_2.domain.entity.*
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

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

    @DELETE("/api/v1/streams/{stream_id}")
    fun archiveStream(
        @Path("stream_id") streamId: Int
    ): Single<StreamDeleteResult>

    @POST("/api/v1/streams/{stream_id}/delete_topic")
    fun removeTopic(
        @Path("stream_id") streamId: Int,
        @Query("topic_name") topicName: String,
    ): Single<TopicDeleteResult>
}