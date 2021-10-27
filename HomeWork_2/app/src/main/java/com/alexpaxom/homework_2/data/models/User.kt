package com.alexpaxom.homework_2.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
    override val typeId: Int = 0,
    val id: Int,
    val name: String,
    val email: String,
    val avatarUrl: String,
    val status: String = "",
    val online: Boolean = false
): Parcelable, ListItem