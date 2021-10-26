package com.alexpaxom.homework_2.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExpandedChanelGroup(
    override val typeId: Int = 0,
    val channel: Channel,
    val topics: List<Topic> = listOf(),
    var isExpanded: Boolean = false,
): Parcelable, ListItem