package com.alexpaxom.homework_2.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alexpaxom.homework_2.data.models.Message
import com.alexpaxom.homework_2.databinding.MessageItemBinding

class ChatHistoryAdapter: BaseAdapter<Message>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Message> {
        return MessageViewHolder(MessageItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    inner class MessageViewHolder(private val messageItemBinding: MessageItemBinding): BaseViewHolder<Message>(messageItemBinding) {
        override fun bind(model: Message) {
            messageItemBinding.messageItem.removeAllReactions()

            model.reactionList.groupingBy { it.emojiUnicode }.eachCount().forEach {
                messageItemBinding.messageItem.addReaction(it.key, it.value)
            }

            messageItemBinding.messageItem.userName = model.userName
            messageItemBinding.messageItem.messageText = model.text
            messageItemBinding.messageItem.setAvatarByUrl(model.avatarUrl ?: "")
        }
    }
}