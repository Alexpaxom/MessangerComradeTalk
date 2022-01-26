package com.alexpaxom.homework_2.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "Users")
data class User (

    @PrimaryKey
    @field:Json(name = "user_id")
    val userId: Int?,

    @ColumnInfo(name="avatar_url")
    @field:Json(name = "avatar_url")
    val avatarUrl: String?,

    @ColumnInfo(name="email")
    @field:Json(name = "email")
    val email: String?,

    @ColumnInfo(name="full_name")
    @field:Json(name = "full_name")
    val fullName: String?,

    @ColumnInfo(name="is_active")
    @field:Json(name = "is_active")
    val isActive: Boolean?,

    @ColumnInfo(name="is_bot")
    @field:Json(name = "is_bot")
    val isBot: Boolean?,

    @ColumnInfo(name = "is_my_user")
    val isMyUser: Boolean? = null
)