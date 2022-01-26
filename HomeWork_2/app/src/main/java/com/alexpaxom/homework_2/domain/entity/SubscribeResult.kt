package com.alexpaxom.homework_2.domain.entity

import com.squareup.moshi.Json

class SubscribeResult {
    @field:Json(name = "msg")
    var msg: String? = null

    @field:Json(name = "result")
    var result: String? = null
}

