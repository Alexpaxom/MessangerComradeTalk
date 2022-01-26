package com.alexpaxom.homework_2.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json


@Entity(tableName = "Topics", primaryKeys = ["name", "channel_id"])
data class Topic (

    @field:Json(name = "name")
    val name: String,

    @ColumnInfo(name = "max_id")
    @field:Json(name = "max_id")
    val maxMessageId: Int?,

    @ColumnInfo(name = "channel_id")
    val channelId: Int,
)