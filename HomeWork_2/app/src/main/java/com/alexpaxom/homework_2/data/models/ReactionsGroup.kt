package com.alexpaxom.homework_2.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReactionsGroup(
    val reactionList: List<Reaction> = listOf(),
    var userIdOwner: Int = 0,
): Parcelable {

    fun getListCount(): List<Pair<String, Int>> {
        return reactionList.groupingBy { it.emojiUnicode }.eachCount().toList()
    }

    fun isSelected(emojiUnicode: String): Boolean {
        return (reactionList.findLast { it.userId == userIdOwner && it.emojiUnicode == emojiUnicode } != null)
    }

    fun addReaction(reaction: Reaction): ReactionsGroup {
        if(reactionList.findLast { it == reaction } != null)
            return this

        return ReactionsGroup(
            reactionList = listOf(*reactionList.toTypedArray(), reaction),
            userIdOwner = userIdOwner
        )
    }

    fun removeReaction(reaction: Reaction): ReactionsGroup {
        return ReactionsGroup(
            reactionList = reactionList.filter { it != reaction },
            userIdOwner = userIdOwner
        )
    }
}