package com.alexpaxom.homework_2.domain.entity

import com.squareup.moshi.Json

class DomainReaction (
    @field:Json(name = "emoji_name")
        val emojiName: String,

    @field:Json(name = "emoji_code")
    val emojiCode: String = "",

    @field:Json(name = "reaction_type")
    val reactionType: String = "",

    @field:Json(name = "user_id")
    val userId: Int = 0
)