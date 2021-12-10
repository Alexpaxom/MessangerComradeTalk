package com.alexpaxom.homework_2.app.fragments

data class ChannelEditState (
    val inProcess: Boolean = false,
    val isFieldNameEmpty: Boolean = false,
    val isSuccess: Boolean = false
)

sealed interface ChannelEditEvent {
    class CreateOrUpdateChannel(
        val channelId: Int? = null,
        val channelName: String,
        val channelDescription: String,
    ): ChannelEditEvent
}

sealed interface ChannelEditEffect {
    class ShowError(val error: String) : ChannelEditEffect
}