package com.alexpaxom.homework_2.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class Message(
    val id: Int,
    val userName: String,
    val text: String,
    val datetime: Date,
    val avatarUrl: String?,
    val reactionList: ArrayList<Reaction> = arrayListOf()
): Parcelable