package com.alexpaxom.homework_2.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.decorators.ItemDecorationCondition
import com.alexpaxom.homework_2.customview.EmojiReactionCounter
import com.alexpaxom.homework_2.customview.MassageViewGroup
import com.alexpaxom.homework_2.data.models.Message
import com.alexpaxom.homework_2.data.models.Reaction
import com.alexpaxom.homework_2.databinding.MessageItemBinding
import com.alexpaxom.homework_2.databinding.MyMessageItemBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ChatHistoryAdapter(): BaseDiffUtilAdapter<Message>(), ItemDecorationCondition<String> {

    private var onReactionClickListener: (EmojiReactionCounter.(message: Message)->Unit)? = null
    private var parentRecycler: RecyclerView? = null


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
            else -> throw Exception("Bad Type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(diffUtil.currentList[position].userId) {
            MY_USER_ID -> R.layout.my_message_item
            else -> R.layout.message_item
        }
    }


    fun addReactionByMessageID(messageId: Int, reaction: Reaction) {
        diffUtil.currentList.indexOfLast { it.id == messageId }.let { messageId ->
            if(messageId != -1) {
                    updateItem(messageId, Message(
                        diffUtil.currentList[messageId],
                        diffUtil.currentList[messageId].reactionsGroup.addReaction(reaction)
                    )
                )
            }
        }
    }

    fun removeReactionByMessageID(messageId: Int, reaction: Reaction) {
        diffUtil.currentList.indexOfLast { it.id == messageId }.let { messageId ->
            if(messageId != -1) {
                updateItem(messageId,
                    Message(
                        diffUtil.currentList[messageId],
                        diffUtil.currentList[messageId].reactionsGroup.removeReaction(reaction)
                    )
                )
            }
        }
    }

    fun setOnReactionClickListener(onClickListener:(EmojiReactionCounter.(message: Message)->Unit)?  = null) {
        onReactionClickListener = onClickListener
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        parentRecycler = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    inner class MessageViewHolder(private val messageItemBinding: MessageItemBinding): BaseViewHolder<Message>(messageItemBinding) {
        override fun bind(model: Message) {
            model.reactionsGroup.userIdOwner = MY_USER_ID
            messageItemBinding.messageItem.setReactions(model.reactionsGroup)
            messageItemBinding.messageItem.userName = model.userName
            messageItemBinding.messageItem.messageText = model.text
            messageItemBinding.messageItem.setAvatarByUrl(model.avatarUrl ?: "")

            messageItemBinding.messageItem.setOnReactionClickListener {
                onReactionClickListener?.invoke(this, model)
            }
        }
    }

    inner class MyMessageViewHolder(private val myMessageItemBinding: MyMessageItemBinding): BaseViewHolder<Message>(myMessageItemBinding) {
        override fun bind(model: Message) {
            model.reactionsGroup.userIdOwner = MY_USER_ID
            myMessageItemBinding.myMessageItem.setReactions(model.reactionsGroup)
            myMessageItemBinding.myMessageItem.messageText = model.text

            myMessageItemBinding.myMessageItem.setOnReactionClickListener {
                onReactionClickListener?.invoke(this, model)
            }
        }
    }

    class MessagesDiffUtil: DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }

    }

    override fun isDecorate(itemPosition: Int): Boolean {
        if(itemPosition == 0)
            return true

        val prevDate = GregorianCalendar()
        val date = GregorianCalendar()
        prevDate.timeInMillis = diffUtil.currentList[itemPosition-1].datetime.time
        date.timeInMillis = diffUtil.currentList[itemPosition].datetime.time
        val diffDays = TimeUnit.MILLISECONDS.toDays(date.timeInMillis - prevDate.timeInMillis)

        return (diffDays > 1 || prevDate.get(Calendar.DAY_OF_MONTH) !=  date.get(Calendar.DAY_OF_MONTH))
    }

    override fun getDecorateParam(itemPosition: Int): String {
        val sf = SimpleDateFormat(DATA_DELIMITER_FORMAT, Locale.ROOT)
        return sf.format(diffUtil.currentList[itemPosition].datetime)
    }

    companion object {
        private const val MY_USER_ID = 99999
        private const val DATA_DELIMITER_FORMAT = "E, dd MMM"
    }

    override fun createDiffUtil(): AsyncListDiffer<Message> {
        val differ = AsyncListDiffer(this, MessagesDiffUtil())

        differ.addListListener { previousList, currentList ->
            if(currentList[currentList.size-1].userId == MY_USER_ID &&
                previousList[previousList.size-1].id != currentList[currentList.size-1].id
            )
                parentRecycler?.scrollToPosition(currentList.size-1)
        }

        return differ
    }

}