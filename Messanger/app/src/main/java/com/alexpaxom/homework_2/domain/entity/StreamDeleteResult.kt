package com.alexpaxom.homework_2.domain.entity

import com.squareup.moshi.Json

class StreamDeleteResult (
    @field:Json(name = "msg")
    val msg: String = "",

    @field:Json(name = "result")
    val result: String,
)