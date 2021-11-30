package com.alexpaxom.homework_2.di.screen

import com.alexpaxom.homework_2.app.activities.MainActivityPresenter
import com.alexpaxom.homework_2.app.fragments.*
import dagger.Subcomponent


@Subcomponent(modules = [ScreenModule::class])
@ScreenScope
interface ScreenComponent {

    fun inject(mainActivityPresenter: MainActivityPresenter)
    fun inject(channelsListAllPresenter: ChannelsListAllPresenter)
    fun inject(channelsListSubscribedPresenter: ChannelsListSubscribedPresenter)
    fun inject(chatPresenter: ChatPresenter)
    fun inject(profilePresenter: ProfilePresenter)
    fun inject(usersPresenter: UsersPresenter)

    @Subcomponent.Factory
    interface Factory {
        fun create(): ScreenComponent
    }
}