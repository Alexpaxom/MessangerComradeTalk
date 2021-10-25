package com.alexpaxom.homework_2.app.adapters.chathistory

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseDiffUtilAdapter
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseHolderFactory
import com.alexpaxom.homework_2.app.adapters.decorators.ItemDecorationCondition
import com.alexpaxom.homework_2.data.models.Message
import com.alexpaxom.homework_2.data.models.Reaction
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ChatHistoryAdapter(
    holdersFactory: BaseHolderFactory
): BaseDiffUtilAdapter<Message>(holdersFactory), ItemDecorationCondition<String> {

    private var parentRecycler: RecyclerView? = null

    init {
        diffUtil = createDiffUtil()
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

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        parentRecycler = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
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

    fun createDiffUtil(): AsyncListDiffer<Message> {
        val differ = AsyncListDiffer(this, MessagesDiffUtil())
        differ.addListListener { previousList, currentList ->
            if(previousList.size != 0 && currentList.size != 0 ) {
                // Передвигаем список в конец при добавлении сообщения пользователем
                if(currentList[currentList.size-1].userId == MY_USER_ID &&
                    previousList[previousList.size-1].id != currentList[currentList.size-1].id
                )
                    parentRecycler?.scrollToPosition(currentList.size-1)
            }
        }

        return differ
    }

}