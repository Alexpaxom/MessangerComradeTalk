package com.alexpaxom.homework_2.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserItem(
    override val typeId: Int = 0,
    val id: Int,
    val name: String,
    val email: String,
    val avatarUrl: String,
    val status: UserStatus = UserStatus(id)
): Parcelable, ListItem