package com.alexpaxom.homework_2.app.features.userprofile.fragments

import com.alexpaxom.homework_2.data.models.UserItem


data class ProfileViewState(
    val user: UserItem? = null,
    val isEmptyLoading: Boolean = false
)

sealed interface ProfileEvent {
    class LoadUserInfo(val userId: Int): ProfileEvent
}

sealed interface ProfileEffect {
    class ShowError(val error: String): ProfileEffect
}