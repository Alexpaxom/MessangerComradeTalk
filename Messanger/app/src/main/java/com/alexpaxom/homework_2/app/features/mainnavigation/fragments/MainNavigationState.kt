package com.alexpaxom.homework_2.app.features.mainnavigation.activities

import com.alexpaxom.homework_2.data.models.UserItem

data class MainNavigationState (
    val userInfo: UserItem? = null,
    val isEmptyLoad: Boolean = false
)

sealed interface MainActivityEvent {
    object GetOwnUserInfo : MainActivityEvent
}

sealed interface MainNavigationEffect {
    class ShowError(val error: String): MainNavigationEffect
}