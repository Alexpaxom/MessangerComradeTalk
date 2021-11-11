package com.alexpaxom.homework_2.data.modelconverters

import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.data.models.ReactionItem
import com.alexpaxom.homework_2.domain.entity.DomainReaction
import com.alexpaxom.homework_2.helpers.EmojiHelper

class ReactionConverter {
    private val emojiHelper = EmojiHelper()

    fun convert(reaction: DomainReaction): ReactionItem {

        val emojiUnicode = try {

            emojiHelper.getUnicodeByName(reaction.emojiName) ?:
            emojiHelper.combineStringToEmoji(reaction.emojiCode)

        } catch(ex: Exception) {
            ""
        }

        return ReactionItem (
            typeId = R.layout.emoji_for_select_view,
            userId = reaction.userId,
            emojiName = reaction.emojiName,
            emojiUnicode = emojiUnicode
        )
    }
}