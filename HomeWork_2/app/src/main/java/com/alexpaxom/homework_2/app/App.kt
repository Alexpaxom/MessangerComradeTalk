package com.alexpaxom.homework_2.app

import android.app.Application
import com.alexpaxom.homework_2.di.application.AppComponent
import com.alexpaxom.homework_2.di.application.DaggerAppComponent

class App: Application() {
    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
        instance = this
    }

    companion object {
        lateinit var instance: App
            private set
    }
}