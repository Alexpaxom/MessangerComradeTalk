package com.alexpaxom.homework_2.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class MessageItem(
    override val typeId: Int = 0,
    val id: Int = 0,
    val userId: Int = 0,
    val userName: String = "",
    val text: String = "",
    val topicName: String = "",
    val datetime: Date = Date(),
    val avatarUrl: String? = null,
    val reactionsGroup: ReactionsGroup = ReactionsGroup()
): Parcelable, ListItem