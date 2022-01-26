package com.alexpaxom.homework_2.app.features.login.fragments

import com.alexpaxom.homework_2.domain.entity.LoginResult


data class LoginState (
    val isEmptyLoad: Boolean = true
)

sealed interface LoginEvent {
    class LogIn(val chatUrl: String? = null, val login: String? = null, val password:String? = null) : LoginEvent
}

sealed interface LoginEffect {
    class ShowError(val error: String): LoginEffect
    class Logined(val loginResult: LoginResult): LoginEffect
}
