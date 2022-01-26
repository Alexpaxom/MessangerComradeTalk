package com.alexpaxom.homework_2.domain.entity

import com.squareup.moshi.Json

class StreamsResult (
    @field:Json(name = "subscriptions")
    val subscriptions: List<Channel> = listOf()
)

