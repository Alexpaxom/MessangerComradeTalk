package com.alexpaxom.homework_2.app.features.chat.fragments

data class ChatParams(
    val topicName: String?,
    val channelName: String,
    val channelId: Int,
    val myUserId: Int
)