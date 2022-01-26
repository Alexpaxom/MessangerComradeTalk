package com.alexpaxom.homework_2.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.alexpaxom.homework_2.domain.converters.ReactionsSerializeConverter
import com.squareup.moshi.Json

@Entity(tableName = "Messages")
@TypeConverters(ReactionsSerializeConverter::class)
class Message (

    @PrimaryKey
    @field:Json(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "sender_id")
    @field:Json(name = "sender_id")
    val senderId: Int = 0,

    @ColumnInfo(name = "content")
    @field:Json(name = "content")
    val content: String = "",

    // UNIX time in UTC SECONDS
    @ColumnInfo(name = "timestamp")
    @field:Json(name = "timestamp")
    val timestamp: Int = 0,

    @ColumnInfo(name = "reactions")
    @field:Json(name = "reactions")
    val reactions: List<DomainReaction> = listOf(),

    @ColumnInfo(name = "sender_full_name")
    @field:Json(name = "sender_full_name")
    val senderFullName: String = "",

    @ColumnInfo(name = "avatar_url")
    @field:Json(name = "avatar_url")
    val avatarUrl: String? = null,

    @ColumnInfo(name = "stream_id")
    @field:Json(name = "stream_id")
    val streamId: Int? = null,

    @ColumnInfo(name = "subject")
    @field:Json(name = "subject")
    val topicName: String? = null,
)