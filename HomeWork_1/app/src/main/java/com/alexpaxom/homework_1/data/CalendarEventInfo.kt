package com.alexpaxom.homework_1.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CalendarEventInfo (
    val title: String = "",
    val dtstart: Long = 0,
    val dtend: Long = 0,
    val organizer: String = "",
): Parcelable