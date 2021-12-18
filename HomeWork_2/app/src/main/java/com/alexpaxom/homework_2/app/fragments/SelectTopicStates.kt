package com.alexpaxom.homework_2.app.fragments

import com.alexpaxom.homework_2.data.models.TopicItem

class SelectTopicState(
    val isEmptyLoading: Boolean = false,
    val topics: List<TopicItem> = listOf(),
)

sealed interface SelectTopicEvent {
    class SearchTopics(
        val channelId: Int,
        val searchString: String,
    ): SelectTopicEvent
}

sealed interface SelectTopicEffect {
    class ShowError(val error: String): SelectTopicEffect
}