package com.alexpaxom.homework_2.data.models

import android.os.Parcelable
import com.alexpaxom.homework_2.R
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserStatus(
    val userId: Int,
    val localAggregatedStatusId: Int = R.string.empty_string,
    val colorId: Int = R.color.white,
): Parcelable