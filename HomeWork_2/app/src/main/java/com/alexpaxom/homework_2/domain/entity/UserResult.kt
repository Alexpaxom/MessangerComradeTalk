package com.alexpaxom.homework_2.domain.entity

import com.squareup.moshi.Json

class UserResult (
    @field:Json(name = "user")
    var user: User
)