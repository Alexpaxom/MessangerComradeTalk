package com.alexpaxom.homework_2.app.adapters.chathistory

import android.util.Log
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseDiffUtilAdapter
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseHolderFactory
import com.alexpaxom.homework_2.data.models.MessageItem
import com.alexpaxom.homework_2.data.models.ReactionItem
import com.alexpaxom.homework_2.helpers.DateFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class ChatHistoryAdapter(
    holdersFactory: BaseHolderFactory
): BaseDiffUtilAdapter<MessageItem>(holdersFactory) {
    init {
        diffUtil = createDiffUtil()
    }


    fun addReactionByMessageID(messageId: Int, reaction: ReactionItem) {
        diffUtil.currentList.indexOfLast { it.id == messageId }.let { messagePos ->
            if(messagePos != -1) {
                updateItem(
                    messagePos,
                    diffUtil.currentList[messagePos].copy (
                        reactionsGroup = diffUtil.currentList[messagePos].reactionsGroup.addReaction(reaction)
                    )
                )
            }
        }
    }

    fun removeReactionByMessageID(messageId: Int, reaction: ReactionItem) {
        diffUtil.currentList.indexOfLast { it.id == messageId }.let { messagePos ->
            if(messagePos != -1) {
                updateItem(
                    messagePos,
                    diffUtil.currentList[messagePos].copy (
                        reactionsGroup = diffUtil.currentList[messagePos].reactionsGroup.removeReaction(reaction)
                    )
                )
            }
        }
    }

    fun addMessagesToPosition(position: Int, messages: List<MessageItem>) {
        val newList = arrayListOf<MessageItem>()
        newList.addAll(dataList)
        newList.addAll(position, messages)
        dataList = newList
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



    fun createDiffUtil(): AsyncListDiffer<MessageItem> {
        val differ = AsyncListDiffer(this, MessagesDiffUtil())
        return differ
    }

    companion object {
        private const val MY_USER_ID = 456094
    }

}