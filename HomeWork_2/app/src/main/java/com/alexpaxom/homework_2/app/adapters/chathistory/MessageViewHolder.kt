package com.alexpaxom.homework_2.app.adapters.chathistory

import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseViewHolder
import com.alexpaxom.homework_2.customview.EmojiReactionCounter
import com.alexpaxom.homework_2.data.models.MessageItem
import com.alexpaxom.homework_2.databinding.MessageItemBinding

class MessageViewHolder(
    val messageItemBinding: MessageItemBinding,
    OnReactionClickListener: (adapterPos: Int, emojiView: EmojiReactionCounter) -> Unit
    ): BaseViewHolder<MessageItem>(messageItemBinding){

    init {
        messageItemBinding.messageItem.setOnReactionClickListener {
            OnReactionClickListener(adapterPosition, this)
        }
    }

    override fun bind(model: MessageItem) {
        messageItemBinding.messageItem.userName = model.userName
        messageItemBinding.messageItem.messageText = model.text
        messageItemBinding.messageItem.setAvatarByUrl(model.avatarUrl.orEmpty())
        messageItemBinding.messageItem.setReactions(model.reactionsGroup)
    }

}