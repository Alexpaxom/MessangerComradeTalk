package com.alexpaxom.homework_1.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CalendarEventInfo (
    var title: String = "",
    var dtstart: Long = 0,
    var dtend: Long = 0,
    var organizer: String = "",
): Parcelable