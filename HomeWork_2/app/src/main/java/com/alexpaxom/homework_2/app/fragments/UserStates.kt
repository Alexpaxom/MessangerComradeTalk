package com.alexpaxom.homework_2.app.fragments

import com.alexpaxom.homework_2.data.models.UserItem

data class UsersViewState (
    val users: List<UserItem> = listOf(),
    val isEmptyLoad: Boolean = false
)

sealed interface UsersEvent {
    class SearchUsers(val searchString: String): UsersEvent
}

sealed interface UsersEffect {
    class ShowError(val error: String): UsersEffect
}