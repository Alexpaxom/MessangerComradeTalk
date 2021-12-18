package com.alexpaxom.homework_2.app.fragments

import com.alexpaxom.homework_2.data.models.MessageItem

data class ChatViewState(
    val messages: List<MessageItem> = listOf(),
    val isEmptyLoading: Boolean = false,
    val editMessage: MessageItem? = null
)


sealed interface ChatEvent {
    class LoadHistory(val chatParams: ChatParams) : ChatEvent

    class ChangedScrollPosition(
        val bottomPos: Int,
        val topPos: Int,
        val chatParams: ChatParams
    ) : ChatEvent

    class SendMessage(val messageItem: MessageItem, val chatParams: ChatParams) : ChatEvent

    class EmojiStateChange(
        val emojiUnicode: String,
        val messageId: Int,
        val isAdd: Boolean,
        val chatParams: ChatParams
    ) : ChatEvent

    class MessageContextMenuSelect(
        val menuItemId: Int,
        val messageItem: MessageItem,
    ) : ChatEvent

    class EditMessage(
        val messageItem: MessageItem? = null,
        val cancel: Boolean = false
    ) : ChatEvent

    object MessagesInserted : ChatEvent
}

sealed interface ChatEffect {
    class ShowEmojiSelector(val messageId: Int) : ChatEffect
    class ShowTopicSelector(val messageItem: MessageItem?) : ChatEffect
    class ShowError(val error: String) : ChatEffect
    class ScrollToPosition(val massageId: Int, val isSmoothScroll: Boolean = false) : ChatEffect
    class SetEditMessageParams(val messageItem: MessageItem) : ChatEffect
    class SetMessageEditField(val text: String): ChatEffect
    class CopyToClipBoard(val text: String): ChatEffect
}