package com.alexpaxom.homework_2.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Message(
    override val typeId: Int = 0,
    val id: Int,
    val userId: Int,
    val userName: String,
    val text: String,
    val datetime: Date,
    val avatarUrl: String?,
    val reactionsGroup: ReactionsGroup = ReactionsGroup()
): Parcelable, ListItem {
    constructor(old: Message, newReactionsGroup: ReactionsGroup) : this(
        old.typeId,
        old.id,
        old.userId,
        old.userName,
        old.text,
        old.datetime,
        old.avatarUrl,
        newReactionsGroup,
    )
}