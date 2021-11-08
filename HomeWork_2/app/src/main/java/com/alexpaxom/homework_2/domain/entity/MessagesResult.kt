package com.alexpaxom.homework_2.domain.entity

import com.squareup.moshi.Json

class MessagesResult (
//    @field:Json(name = "result")
//        val String result
//    @field:Json(name = "msg")
//        val String msg
    @field:Json(name = "messages")
        val messages: List<Message> = listOf()
//    @field:Json(name = "found_anchor")
//        val Boolean foundAnchor
//    @field:Json(name = "found_oldest")
//        val Boolean foundOldest
//    @field:Json(name = "found_newest")
//        val Boolean foundNewest
//    @field:Json(name = "history_limited")
//        val Boolean historyLimited
//    @field:Json(name = "anchor")
//        val Integer anchor

)