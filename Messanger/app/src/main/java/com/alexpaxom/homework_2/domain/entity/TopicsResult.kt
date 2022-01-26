package com.alexpaxom.homework_2.domain.entity

import com.squareup.moshi.Json

class TopicsResult {
    @field:Json(name = "topics")
    val topics: List<Topic> = listOf()
}


