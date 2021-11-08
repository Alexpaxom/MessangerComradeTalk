package com.alexpaxom.homework_2.domain.entity

import com.squareup.moshi.Json

class Message (
    @field:Json(name = "id")
    val id: Int = 0,

    @field:Json(name = "sender_id")
    val senderId: Int = 0,

    @field:Json(name = "content")
    val content: String = "",

//    @field:Json(name = "recipient_id")
//        val recipientId: Int? = null
//

    // UNIX time in UTC SECONDS
    @field:Json(name = "timestamp")
    val timestamp: Int = 0,

//    @field:Json(name = "client")
//        val client: String? = null
//
//    @field:Json(name = "subject")
//        val subject: String? = null
//
//    @field:Json(name = "topic_links")
//        val topicLinks: List<Any>? = null
//
    @field:Json(name = "is_me_message")
    val isMeMessage: Boolean = false,

    @field:Json(name = "reactions")
    val reactions: List<DomainReaction> = listOf(),

//    @field:Json(name = "submessages")
//        val submessages: List<Any>? = null
//
//    @field:Json(name = "flags")
//        val flags: List<String>? = null

    @field:Json(name = "sender_full_name")
    val senderFullName: String = "",
//
//    @field:Json(name = "sender_email")
//        val senderEmail: String? = null
//
//    @field:Json(name = "sender_realm_str")
//        val senderRealmStr: String? = null
//
//    @field:Json(name = "display_recipient")
//        val displayRecipient: String? = null
//
//    @field:Json(name = "type")
//        val type: String? = null
//
//    @field:Json(name = "stream_id")
//        val streamId: Int? = null

    @field:Json(name = "avatar_url")
    val avatarUrl: String? = null,

//    @field:Json(name = "content_type")
//        val contentType: String? = null
)