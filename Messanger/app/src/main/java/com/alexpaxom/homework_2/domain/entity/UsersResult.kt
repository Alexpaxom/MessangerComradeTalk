package com.alexpaxom.homework_2.domain.entity

import com.squareup.moshi.Json

class UsersResult (
    @field:Json(name = "members")
    var members: List<User> = listOf()
)
