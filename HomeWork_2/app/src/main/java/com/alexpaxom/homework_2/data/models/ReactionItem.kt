package com.alexpaxom.homework_2.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReactionItem(
    override val typeId: Int = 0,
    val userId: Int,
    val emojiName: String,
    val emojiUnicode: String
): Parcelable, ListItem