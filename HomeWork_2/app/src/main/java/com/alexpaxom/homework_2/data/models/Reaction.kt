package com.alexpaxom.homework_2.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Reaction(
    override val typeId: Int = 0,
    val userId: Int,
    val emojiUnicode: String
): Parcelable, ListItem