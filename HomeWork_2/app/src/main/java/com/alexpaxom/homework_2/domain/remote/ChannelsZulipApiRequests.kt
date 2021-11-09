package com.alexpaxom.homework_2.domain.remote

import com.alexpaxom.homework_2.domain.entity.StreamsResult
import com.alexpaxom.homework_2.domain.entity.TopicsResult
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ChannelsZulipApiRequests {
    @GET("/api/v1/users/me/subscriptions")
    fun getSubscribedStreams(): Single<StreamsResult>

    @GET("/api/v1/streams")
    fun getAllStreams(): Single<StreamsResult>

    @GET("/api/v1/users/me/{stream_id}/topics")
    fun getTopicsByStreamId(
        @Path("stream_id") streamId: Int
    ): Single<TopicsResult>
}