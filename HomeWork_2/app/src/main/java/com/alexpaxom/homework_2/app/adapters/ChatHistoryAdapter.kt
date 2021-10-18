package com.alexpaxom.homework_2.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alexpaxom.homework_2.customview.EmojiReactionCounter
import com.alexpaxom.homework_2.customview.MassageViewGroup
import com.alexpaxom.homework_2.data.models.Message
import com.alexpaxom.homework_2.data.models.Reaction
import com.alexpaxom.homework_2.databinding.MessageItemBinding

class ChatHistoryAdapter: BaseAdapter<Message>() {

    private var onReactionClickListener: (EmojiReactionCounter.(parentMessage: MassageViewGroup, model: Message)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Message> {
        return MessageViewHolder(MessageItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    fun setOnReactionClickListener(onClickListener:(EmojiReactionCounter.(parentMessage: MassageViewGroup, model: Message)->Unit)?  = null) {
        onReactionClickListener = onClickListener
    }

    inner class MessageViewHolder(private val messageItemBinding: MessageItemBinding): BaseViewHolder<Message>(messageItemBinding) {
        override fun bind(model: Message) {
            messageItemBinding.messageItem.removeAllReactions()
            model.reactionList.groupingBy { it.emojiUnicode }.eachCount().forEach {
                messageItemBinding.messageItem.addReaction(it.key, it.value, model.reactionList.contains(
                    Reaction(
                        MY_USER_ID,
                        it.key
                    )
                ))
            }

            messageItemBinding.messageItem.userName = model.userName
            messageItemBinding.messageItem.messageText = model.text
            messageItemBinding.messageItem.setAvatarByUrl(model.avatarUrl ?: "")

            messageItemBinding.messageItem.setOnReactionClickListener {
                onReactionClickListener?.invoke(this, messageItemBinding.messageItem, model)
            }
        }
    }

    companion object {
        private const val MY_USER_ID = 99999
    }
}