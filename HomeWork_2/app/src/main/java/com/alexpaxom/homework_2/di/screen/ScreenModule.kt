package com.alexpaxom.homework_2.di.screen

import com.alexpaxom.homework_2.domain.cache.CacheDatabase
import com.alexpaxom.homework_2.domain.cache.daos.*
import com.alexpaxom.homework_2.domain.remote.ChannelsZulipApiRequests
import com.alexpaxom.homework_2.domain.remote.LoginZulipApiRequests
import com.alexpaxom.homework_2.domain.remote.MessagesZulipApiRequests
import com.alexpaxom.homework_2.domain.remote.UsersZulipApiRequests
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class ScreenModule {
    // Zulip Api provides
    @Provides
    fun provideUsersZulipApiRequests(retrofit: Retrofit): UsersZulipApiRequests {
        return retrofit.create(UsersZulipApiRequests::class.java)
    }

    @Provides
    fun provideChannelsZulipApiRequests(retrofit: Retrofit): ChannelsZulipApiRequests {
        return retrofit.create(ChannelsZulipApiRequests::class.java)
    }

    @Provides
    fun provideMessagesZulipApiRequests(retrofit: Retrofit): MessagesZulipApiRequests {
        return retrofit.create(MessagesZulipApiRequests::class.java)
    }

    @Provides
    fun provideLoginZulipApiRequests(retrofit: Retrofit): LoginZulipApiRequests {
        return retrofit.create(LoginZulipApiRequests::class.java)
    }


    // Cache database entities provides
    @Provides
    fun provideUsersDAO(cacheDatabase: CacheDatabase): UsersDAO {
        return cacheDatabase.usersDao()
    }

    @Provides
    fun provideTopicsDAO(cacheDatabase: CacheDatabase): TopicsDAO {
        return cacheDatabase.topicsDao()
    }

    @Provides
    fun provideMessagesDAO(cacheDatabase: CacheDatabase): MessagesDAO {
        return cacheDatabase.messagesDao()
    }

    @Provides
    fun provideChannelsDAO(cacheDatabase: CacheDatabase): ChannelsDAO {
        return cacheDatabase.channelsDao()
    }

    @Provides
    fun provideLoginDAO(cacheDatabase: CacheDatabase): LoginDAO {
        return cacheDatabase.loginDAO()
    }
}