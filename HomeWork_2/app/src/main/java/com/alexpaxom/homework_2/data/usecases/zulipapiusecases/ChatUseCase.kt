package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.data.models.MessageItem
import com.alexpaxom.homework_2.data.models.ReactionItem
import com.alexpaxom.homework_2.di.screen.ScreenScope
import javax.inject.Inject

@ScreenScope
class ChatUseCase @Inject constructor(){

    fun addReactionByMessageID(
        messageId: Int,
        reaction: ReactionItem,
        messages: List<MessageItem>
    ): List<MessageItem> {

        val ret = arrayListOf<MessageItem>()
        ret.addAll(messages)

        ret.indexOfLast { it.id == messageId }.let { messagePos ->
            if(messagePos != -1) {
                ret[messagePos] =
                    ret[messagePos].copy (
                        reactionsGroup = ret[messagePos].reactionsGroup.addReaction(reaction)
                    )
            }
        }

        return ret.toList()
    }

    fun removeReactionByMessageID(
        messageId: Int,
        reaction: ReactionItem,
        messages: List<MessageItem>
    ): List<MessageItem> {

        val ret = arrayListOf<MessageItem>()
        ret.addAll(messages)

        ret.indexOfLast { it.id == messageId }.let { messagePos ->
            if(messagePos != -1) {
                ret[messagePos] =
                    ret[messagePos].copy (
                        reactionsGroup = ret[messagePos].reactionsGroup.removeReaction(reaction)
                    )
            }
        }

        return ret.toList()
    }

    fun addMessagesToPosition(
        position: Int,
        newMessages: List<MessageItem>,
        messages: List<MessageItem>
    ): List<MessageItem> {
        val ret = arrayListOf<MessageItem>()
        ret.addAll(messages)
        ret.addAll(position, newMessages)
        return ret
    }


}