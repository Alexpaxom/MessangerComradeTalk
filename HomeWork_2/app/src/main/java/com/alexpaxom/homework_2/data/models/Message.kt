package com.alexpaxom.homework_2.data.models

data class Message(
    val id: Int,
    val userName: String,
    val text: String,
    val avatarUrl: String?,
)