package com.alexpaxom.homework_2.domain.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Entity(tableName = "LoginParams")
@Parcelize
data class LoginResult (
    @PrimaryKey
    @field:Json(name = "email")
    @ColumnInfo(name = "email")
    val email: String,

    @field:Json(name = "api_key")
    @ColumnInfo(name = "api_key")
    val apiKey: String,

    @ColumnInfo(name = "url")
    val url: String = ""
): Parcelable