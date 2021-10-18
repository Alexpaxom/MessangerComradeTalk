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
            messageItemBinding.messageItem.addReaction(count=99)
            messageItemBinding.messageItem.userName = model.userName
            messageItemBinding.messageItem.messageText = model.text
            messageItemBinding.messageItem.setAvatarByUrl(model.avatarUrl ?: "https://secure.gravatar.com/avatar/4c00913fa20d9871e41cb5ce82576585?s=80&d=identicon")
        }
    }
}