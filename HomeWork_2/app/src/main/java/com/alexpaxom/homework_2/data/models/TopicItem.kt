package com.alexpaxom.homework_2.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopicItem(
    override val typeId: Int = 0,
    override val id: Int,
    val channelId: Int,
    val name: String,
    val countMessages: Int = 0,
): Parcelable, ExpandedChannelItem