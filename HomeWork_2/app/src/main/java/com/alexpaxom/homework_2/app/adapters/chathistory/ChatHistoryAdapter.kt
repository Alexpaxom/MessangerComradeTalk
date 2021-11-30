package com.alexpaxom.homework_2.app.adapters.chathistory

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseDiffUtilAdapter
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseHolderFactory
import com.alexpaxom.homework_2.data.models.MessageItem
import com.alexpaxom.homework_2.helpers.DateFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class ChatHistoryAdapter(
    holdersFactory: BaseHolderFactory
): BaseDiffUtilAdapter<MessageItem>(holdersFactory) {

    init {
        diffUtil = AsyncListDiffer(this, MessagesDiffUtil())
    }

    fun isDecorate(itemPosition: Int): Boolean {
        if(itemPosition == 0)
            return true

        val prevDate = GregorianCalendar()
        val date = GregorianCalendar()
        prevDate.timeInMillis = dataList[itemPosition-1].datetime.time
        date.timeInMillis = dataList[itemPosition].datetime.time
        val diffDays = TimeUnit.MILLISECONDS.toDays(date.timeInMillis - prevDate.timeInMillis)

        return diffDays > 1 || prevDate.get(Calendar.DAY_OF_MONTH) !=  date.get(Calendar.DAY_OF_MONTH)
    }

    fun getDecorateParam(itemPosition: Int): String {
        return DateFormatter.formatDate (
            dataList[itemPosition].datetime, DateFormatter.DATA_DELIMITER_FORMAT
        )
    }

    class MessagesDiffUtil: DiffUtil.ItemCallback<MessageItem>() {
        override fun areItemsTheSame(oldItem: MessageItem, newItem: MessageItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MessageItem, newItem: MessageItem): Boolean {
            return oldItem == newItem
        }
    }
}