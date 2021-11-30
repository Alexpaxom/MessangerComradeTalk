package com.alexpaxom.homework_2.app.adapters.chathistory

import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseViewHolder
import com.alexpaxom.homework_2.customview.EmojiReactionCounter
import com.alexpaxom.homework_2.data.models.MessageItem
import com.alexpaxom.homework_2.databinding.MyMessageItemBinding

class MyMessageViewHolder(
    val myMessageItemBinding: MyMessageItemBinding,
    OnReactionClickListener: (adapterPos: Int, emojiView: EmojiReactionCounter) -> Unit
): BaseViewHolder<MessageItem>(myMessageItemBinding){

    init {
        myMessageItemBinding.myMessageItem.setOnReactionClickListener {
            OnReactionClickListener(adapterPosition, this)
        }
    }

    override fun bind(model: MessageItem) {
        myMessageItemBinding.myMessageItem.messageText = model.text
        myMessageItemBinding.myMessageItem.setReactions(model.reactionsGroup)
    }
}