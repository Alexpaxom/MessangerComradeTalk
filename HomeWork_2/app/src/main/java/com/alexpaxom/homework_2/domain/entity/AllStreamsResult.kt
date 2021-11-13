package com.alexpaxom.homework_2.domain.entity

import com.squareup.moshi.Json

class AllStreamsResult (
    @field:Json(name = "streams")
    val streams: List<Channel> = listOf()
)