package com.alexpaxom.homework_2.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserStatus(
    val userId: Int,
    val aggregatedStatus: String
): Parcelable