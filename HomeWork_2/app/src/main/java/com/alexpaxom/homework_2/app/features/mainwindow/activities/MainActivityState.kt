package com.alexpaxom.homework_2.app.features.mainwindow.activities

import com.alexpaxom.homework_2.data.models.UserItem

data class MainActivityState (
    val userInfo: UserItem? = null,
    val isEmptyLoad: Boolean = false
)

sealed interface MainActivityEvent {
    object GetOwnUserInfo : MainActivityEvent
}

sealed interface MainActivityEffect {
    class ShowError(val error: String): MainActivityEffect
}