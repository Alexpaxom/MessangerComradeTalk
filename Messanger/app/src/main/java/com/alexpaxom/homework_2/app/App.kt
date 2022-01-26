package com.alexpaxom.homework_2.app

import android.app.Application
import com.alexpaxom.homework_2.di.application.AppComponent
import com.alexpaxom.homework_2.di.application.DaggerAppComponent
import com.alexpaxom.homework_2.domain.entity.LoginResult

class App: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this, LoginResult("", "", "https://alexpaxom.com"))
    }

    fun reCreateAppComponent(loginResult: LoginResult) {
        appComponent = DaggerAppComponent.factory().create(this, loginResult)
    }
}