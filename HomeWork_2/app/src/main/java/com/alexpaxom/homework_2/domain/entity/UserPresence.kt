package com.alexpaxom.homework_2.domain.entity

import com.squareup.moshi.Json

class UserPresence (
    @field:Json(name = "presence")
    var presence: Presence
)

class Presence (
    @field:Json(name = "aggregated")
    var aggregated: Aggregated
)

class Aggregated (
    @field:Json(name = "status")
    var status: String
)



