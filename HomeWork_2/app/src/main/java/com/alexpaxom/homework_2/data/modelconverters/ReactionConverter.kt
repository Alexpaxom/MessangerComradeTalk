package com.alexpaxom.homework_2.data.modelconverters

import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.data.models.ReactionItem
import com.alexpaxom.homework_2.domain.entity.DomainReaction

class ReactionConverter {
    fun convert(reaction: DomainReaction): ReactionItem {
        return ReactionItem (
                    typeId = R.layout.emoji_for_select_view,
                    userId = reaction.userId,
                    emojiName = reaction.emojiName,
                    emojiUnicode = String(Character.toChars(reaction.emojiCode.toInt(16)))
               )
    }
}