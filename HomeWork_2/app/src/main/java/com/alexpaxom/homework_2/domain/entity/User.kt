package com.alexpaxom.homework_2.domain.entity

import com.squareup.moshi.Json


class User (
    @field:Json(name = "avatar_url")
    var avatarUrl: String,
//
//    @field:Json(name = "bot_type")
//    var botType: Int? = null
//
//    @field:Json(name = "date_joined")
//    var dateJoined: String? = null

    @field:Json(name = "email")
    var email: String,

    @field:Json(name = "full_name")
    var fullName: String,

    @field:Json(name = "is_active")
    var isActive: Boolean,
//
//    @field:Json(name = "is_admin")
//    var isAdmin: Boolean? = null
//
//    @field:Json(name = "is_billing_admin")
//    var isBillingAdmin: Boolean? = null
//
    @field:Json(name = "is_bot")
    var isBot: Boolean,

//    @field:Json(name = "is_guest")
//    var isGuest: Boolean? = null
//
//    @field:Json(name = "is_owner")
//    var isOwner: Boolean? = null
//
//    @field:Json(name = "profile_data")
//    var profileData: ProfileData? = null
//
//    @field:Json(name = "role")
//    var role: Int? = null
//
//    @field:Json(name = "timezone")
//    var timezone: String? = null

    @field:Json(name = "user_id")
    var userId: Int,
//
//    @field:Json(name = "bot_owner_id")
//    var botOwnerId: Int? = null
    )