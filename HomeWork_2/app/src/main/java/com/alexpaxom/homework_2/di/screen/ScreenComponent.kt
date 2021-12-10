package com.alexpaxom.homework_2.di.screen

import com.alexpaxom.homework_2.app.activities.MainActivity
import com.alexpaxom.homework_2.app.fragments.*
import dagger.Subcomponent


@Subcomponent(modules = [ScreenModule::class])
@ScreenScope
interface ScreenComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(channelsListSubscribedFragment: ChannelsListSubscribedFragment)
    fun inject(channelsListAllFragment: ChannelsListAllFragment)
    fun inject(chatFragment: ChatFragment)
    fun inject(profileFragment: ProfileFragment)
    fun inject(usersFragment: UsersFragment)
    fun inject(channelEditFragment: ChannelEditFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): ScreenComponent
    }
}