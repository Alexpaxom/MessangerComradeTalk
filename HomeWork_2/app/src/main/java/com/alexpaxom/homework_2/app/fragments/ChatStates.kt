package com.alexpaxom.homework_2.app.fragments

import com.alexpaxom.homework_2.data.models.MessageItem

data class ChatViewState(
    val messages: List<MessageItem> = listOf(),
    val isEmptyLoading: Boolean = false
)



sealed interface ChatEvent {
    class LoadHistory(val chatParams: ChatParams): ChatEvent

    class ChangedScrollPosition(
        val bottomPos: Int,
        val topPos: Int,
        val chatParams: ChatParams): ChatEvent

    class SendMessage(val message: String, val chatParams: ChatParams): ChatEvent

    class EmojiStateChange(
        val emojiUnicode: String,
        val messageId: Int,
        val isAdd: Boolean,
        val chatParams: ChatParams): ChatEvent



    object MessagesInserted: ChatEvent
}

sealed interface ChatEffect {
    class ShowError(val error: String): ChatEffect
    class ScrollToPosition(val massageId: Int, val isSmoothScroll: Boolean = false): ChatEffect
}