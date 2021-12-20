package com.alexpaxom.homework_2.app.features.chat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.features.baseelements.adapters.BaseHolderFactory
import com.alexpaxom.homework_2.app.features.baseelements.adapters.BaseViewHolder
import com.alexpaxom.homework_2.customview.EmojiReactionCounter
import com.alexpaxom.homework_2.databinding.MessageItemBinding
import com.alexpaxom.homework_2.databinding.MyMessageItemBinding

class ChatMessageFactory(
    val longClickCallback: (adapterPos: Int) -> Unit,
    val onReactionClickListener: (adapterPos: Int, emojiView: EmojiReactionCounter) -> Unit
): BaseHolderFactory() {
    override fun createViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val holder = when(viewType) {
            R.layout.message_item -> MessageViewHolder(
                    MessageItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                        viewGroup,
                    false
                ),
                onReactionClickListener
            )
            R.layout.my_message_item -> MyMessageViewHolder(
                    MyMessageItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                        viewGroup,
                    false
                ),
                onReactionClickListener
            )
            else -> error("Bad type message chat holder")
        }

        holder.itemView.setOnLongClickListener {
            longClickCallback(holder.absoluteAdapterPosition)
            true
        }

        return holder
    }
}