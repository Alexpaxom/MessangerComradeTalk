package com.alexpaxom.homework_2.di.screen

import com.alexpaxom.homework_2.app.features.channels.fragments.ChannelEditFragment
import com.alexpaxom.homework_2.app.features.channels.fragments.ChannelsListAllFragment
import com.alexpaxom.homework_2.app.features.channels.fragments.ChannelsListSubscribedFragment
import com.alexpaxom.homework_2.app.features.chat.fragments.ChatFragment
import com.alexpaxom.homework_2.app.features.login.fragments.LoginFragment
import com.alexpaxom.homework_2.app.MainActivity
import com.alexpaxom.homework_2.app.features.mainnavigation.fragments.MainNavigationFragment
import com.alexpaxom.homework_2.app.features.userprofile.fragments.ProfileFragment
import com.alexpaxom.homework_2.app.features.userslist.fragments.UsersFragment
import com.alexpaxom.homework_2.app.features.topicselector.fragments.*
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
    fun inject(selectTopicFragment: SelectTopicFragment)
    fun inject(mainNavigationFragment: MainNavigationFragment)
    fun inject(loginFragment: LoginFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): ScreenComponent
    }
}