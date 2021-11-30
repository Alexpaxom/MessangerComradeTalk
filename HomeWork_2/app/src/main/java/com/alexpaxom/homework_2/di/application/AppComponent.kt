package com.alexpaxom.homework_2.di.application

import com.alexpaxom.homework_2.app.App
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            application: App
        ): AppComponent
    }
}