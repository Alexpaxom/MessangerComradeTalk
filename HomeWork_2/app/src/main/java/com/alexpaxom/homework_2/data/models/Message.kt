package com.alexpaxom.homework_2.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Message(
    val id: Int,
    val userId: Int,
    val userName: String,
    val text: String,
    val datetime: Date,
    val avatarUrl: String?,
    val reactionsGroup: ReactionsGroup = ReactionsGroup()
): Parcelable {
    constructor(old: Message, newReactionsGroup: ReactionsGroup) : this(
        old.id,
        old.userId,
        old.userName,
        old.text,
        old.datetime,
        old.avatarUrl,
        newReactionsGroup,
    )
}