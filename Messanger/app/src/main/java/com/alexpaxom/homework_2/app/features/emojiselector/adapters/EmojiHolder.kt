package com.alexpaxom.homework_2.app.features.emojiselector.adapters

import com.alexpaxom.homework_2.app.features.baseelements.adapters.BaseViewHolder
import com.alexpaxom.homework_2.data.models.ReactionItem
import com.alexpaxom.homework_2.databinding.EmojiForSelectViewBinding

class EmojiHolder(
    val emojiForSelectViewBinding: EmojiForSelectViewBinding
    ): BaseViewHolder<ReactionItem>(emojiForSelectViewBinding) {
    override fun bind(model: ReactionItem) {
        emojiForSelectViewBinding.emojiItem.text = model.emojiUnicode
    }
}