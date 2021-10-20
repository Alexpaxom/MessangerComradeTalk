package com.alexpaxom.homework_2.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.decorators.DecorationCondition
import com.alexpaxom.homework_2.customview.EmojiReactionCounter
import com.alexpaxom.homework_2.customview.MassageViewGroup
import com.alexpaxom.homework_2.data.models.Message
import com.alexpaxom.homework_2.data.models.Reaction
import com.alexpaxom.homework_2.databinding.MessageItemBinding
import com.alexpaxom.homework_2.databinding.MyMessageItemBinding
import java.util.*
import java.util.concurrent.TimeUnit

class ChatHistoryAdapter: BaseAdapter<Message>(), DecorationCondition {

    private var onReactionClickListener: (EmojiReactionCounter.(parentMessage: MassageViewGroup, model: Message)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Message> {
        return when(viewType) {
            R.layout.message_item -> {
                MessageViewHolder(MessageItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ))
            }
            R.layout.my_message_item -> {
                MyMessageViewHolder(MyMessageItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ))
            }
            else -> throw Exception("Bed Type")
        }
    }

    fun getItemAt(index: Int): Message {
        return dataList[index]
    }

    override fun getItemViewType(position: Int): Int {
        return when(dataList[position].id) {
            in 0..999 -> R.layout.message_item
            in 1000..2000 -> R.layout.my_message_item
            else -> throw Exception("Bed Type")
        }
    }


    fun addReactionByMessageID(messageId: Int, reaction: Reaction) {
        dataList.indexOfLast {
            it.id == messageId
        }.let {
            if(it != -1) {
                if(dataList[it].reactionList.findLast { r -> r == reaction } == null) {
                    dataList[it].reactionList.add(reaction)
                    notifyItemChanged(it)
                }
            }
        }
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

    inner class MyMessageViewHolder(private val myMessageItemBinding: MyMessageItemBinding): BaseViewHolder<Message>(myMessageItemBinding) {
        override fun bind(model: Message) {
            myMessageItemBinding.myMessageItem.removeAllReactions()
            model.reactionList.groupingBy { it.emojiUnicode }.eachCount().forEach {
                myMessageItemBinding.myMessageItem.addReaction(it.key, it.value, model.reactionList.contains(
                    Reaction(
                        MY_USER_ID,
                        it.key
                    )
                ))
            }

            myMessageItemBinding.myMessageItem.userName = model.userName
            myMessageItemBinding.myMessageItem.messageText = model.text
            myMessageItemBinding.myMessageItem.setAvatarByUrl(model.avatarUrl ?: "")

            myMessageItemBinding.myMessageItem.setOnReactionClickListener {
                onReactionClickListener?.invoke(this, myMessageItemBinding.myMessageItem, model)
            }
        }
    }

    companion object {
        private const val MY_USER_ID = 99999
    }

    override fun isDecorate(itemPosition: Int): Boolean {
        if(itemPosition == 0)
            return true

        val prevDate = GregorianCalendar()
        val date = GregorianCalendar()
        prevDate.timeInMillis = dataList[itemPosition-1].datetime.time
        date.timeInMillis = dataList[itemPosition].datetime.time
        val diffDays = TimeUnit.MILLISECONDS.toDays(date.timeInMillis - prevDate.timeInMillis)

        return (diffDays > 1 || prevDate.get(Calendar.DAY_OF_MONTH) !=  date.get(Calendar.DAY_OF_MONTH))
    }
}