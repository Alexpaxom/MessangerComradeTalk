package com.alexpaxom.homework_2.app.adapters.emojiselector

import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseViewHolder
import com.alexpaxom.homework_2.data.models.Reaction
import com.alexpaxom.homework_2.databinding.EmojiForSelectViewBinding

class EmojiHolder(
    val emojiForSelectViewBinding: EmojiForSelectViewBinding
    ): BaseViewHolder<Reaction>(emojiForSelectViewBinding) {
    override fun bind(model: Reaction) {
        emojiForSelectViewBinding.emojiItem.text = model.emojiUnicode
    }
}