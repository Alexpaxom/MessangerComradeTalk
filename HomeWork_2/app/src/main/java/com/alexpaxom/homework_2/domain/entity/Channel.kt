package com.alexpaxom.homework_2.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.squareup.moshi.Json

@Entity(tableName = "Channels", primaryKeys = ["stream_id", "in_subscribed"])
data class Channel (
    @ColumnInfo(name="stream_id")
    @field:Json(name = "stream_id")
    val streamId: Int,

    @ColumnInfo(name="name")
    @field:Json(name = "name")
    val name: String?,

    @ColumnInfo(name="in_subscribed")
    val inSubscribes: Boolean = false
)