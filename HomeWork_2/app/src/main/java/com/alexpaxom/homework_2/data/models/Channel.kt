package com.alexpaxom.homework_2.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Channel(
    override val typeId: Int = 0,
    val id: Int,
    val name: String,
): Parcelable, ListItem