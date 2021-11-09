package com.alexpaxom.homework_2.domain.entity

import com.squareup.moshi.Json

class Channel (
//    @field:Json(name = "audible_notifications")
//    val audibleNotifications: Boolean? = null
//
//    @field:Json(name = "color")
//    val color: String? = null
//
//    @field:Json(name = "description")
//    val description: String? = null
//
//    @field:Json(name = "desktop_notifications")
//    val desktopNotifications: Boolean? = null
//
//    @field:Json(name = "email_address")
//    val emailAddress: String? = null
//
//    @field:Json(name = "invite_only")
//    val inviteOnly: Boolean? = null
//
//    @field:Json(name = "is_muted")
//    val isMuted: Boolean? = null

    @field:Json(name = "name")
    val name: String,
//
//    @field:Json(name = "pin_to_top")
//    val pinToTop: Boolean? = null
//
//    @field:Json(name = "push_notifications")
//    val pushNotifications: Boolean? = null
//
//    @field:Json(name = "role")
//    val role: Int? = null

    @field:Json(name = "stream_id")
    val streamId: Int,
//
//    @field:Json(name = "subscribers")
//    val subscribers: List<Int>? = null
)