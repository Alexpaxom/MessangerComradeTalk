package com.alexpaxom.homework_2.data.modelconverters

import android.os.Build
import android.text.Html
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.data.models.MessageItem
import com.alexpaxom.homework_2.data.models.ReactionsGroup
import com.alexpaxom.homework_2.domain.entity.Message
import java.util.*

class MessageConverter {

    val reactionConverter = ReactionConverter()

    fun convert(message: Message): MessageItem {
        val text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                Html.fromHtml(message.content, Html.FROM_HTML_MODE_COMPACT)
            else
                Html.fromHtml(message.content)

        val reactionsList = message.reactions
            .filter{it.reactionType == "unicode_emoji"}
            .map{ reactionConverter.convert(it) }


        return MessageItem(
                typeId = R.layout.message_item,
                id = message.id,
                userId = message.senderId,
                userName = message.senderFullName,
                text = text.toString(),
                datetime = Date(message.timestamp*CONVERT_FROM_UTC_SECONDS),
                avatarUrl = message.avatarUrl,
                reactionsGroup = ReactionsGroup(reactionsList)
            )
    }

    companion object {
        private const val CONVERT_FROM_UTC_SECONDS = 1000L
    }

}