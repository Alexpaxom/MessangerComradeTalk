package com.alexpaxom.homework_2.app.adapters.chathistory

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseDiffUtilAdapter
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseHolderFactory
import com.alexpaxom.homework_2.data.models.MessageItem
import com.alexpaxom.homework_2.data.models.ReactionItem
import com.alexpaxom.homework_2.helpers.DateFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class ChatHistoryAdapter(
    holdersFactory: BaseHolderFactory
): BaseDiffUtilAdapter<MessageItem>(holdersFactory) {
    private var addMessageClickListener: ((messagePos: Int) -> Unit)? = null

    init {
        diffUtil = createDiffUtil()
    }


    fun addReactionByMessageID(messageId: Int, reaction: ReactionItem) {
        diffUtil.currentList.indexOfLast { it.id == messageId }.let { messageId ->
            if(messageId != -1) {
                    updateItem(messageId, MessageItem(
                        diffUtil.currentList[messageId],
                        diffUtil.currentList[messageId].reactionsGroup.addReaction(reaction)
                    )
                )
            }
        }
    }

    fun removeReactionByMessageID(messageId: Int, reaction: ReactionItem) {
        diffUtil.currentList.indexOfLast { it.id == messageId }.let { messageId ->
            if(messageId != -1) {
                updateItem(messageId,
                    MessageItem(
                        diffUtil.currentList[messageId],
                        diffUtil.currentList[messageId].reactionsGroup.removeReaction(reaction)
                    )
                )
            }
        }
    }


    class MessagesDiffUtil: DiffUtil.ItemCallback<MessageItem>() {
        override fun areItemsTheSame(oldItem: MessageItem, newItem: MessageItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MessageItem, newItem: MessageItem): Boolean {
            return oldItem == newItem
        }

    }

    fun isDecorate(itemPosition: Int): Boolean {
        if(itemPosition == 0)
            return true

        val prevDate = GregorianCalendar()
        val date = GregorianCalendar()
        prevDate.timeInMillis = diffUtil.currentList[itemPosition-1].datetime.time
        date.timeInMillis = diffUtil.currentList[itemPosition].datetime.time
        val diffDays = TimeUnit.MILLISECONDS.toDays(date.timeInMillis - prevDate.timeInMillis)

        return diffDays > 1 || prevDate.get(Calendar.DAY_OF_MONTH) !=  date.get(Calendar.DAY_OF_MONTH)
    }

    fun getDecorateParam(itemPosition: Int): String {
        return DateFormatter.formatDate (
            diffUtil.currentList[itemPosition].datetime, DateFormatter.DATA_DELIMITER_FORMAT
        )
    }

    companion object {
        private const val MY_USER_ID = 99999
    }

    fun setClickListenerOnAddMessage(listener: (messagePos: Int) -> Unit ){
        addMessageClickListener = listener
    }

    fun createDiffUtil(): AsyncListDiffer<MessageItem> {
        val differ = AsyncListDiffer(this, MessagesDiffUtil())
        differ.addListListener { previousList, currentList ->
            if(previousList.size != 0 && currentList.size != 0 ) {
                // Передвигаем список в конец при добавлении сообщения пользователем
                if(currentList[currentList.size-1].userId == MY_USER_ID &&
                    previousList[previousList.size-1].id != currentList[currentList.size-1].id
                ){
                    addMessageClickListener?.invoke(currentList.size-1)
                }

            }
        }

        return differ
    }

}