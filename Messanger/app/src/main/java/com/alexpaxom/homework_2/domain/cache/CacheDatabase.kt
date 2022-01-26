package com.alexpaxom.homework_2.domain.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alexpaxom.homework_2.domain.cache.daos.*
import com.alexpaxom.homework_2.domain.entity.*

@Database(
    entities = [Message::class, User::class, Topic::class, Channel::class, LoginResult::class],
    version = 8,
    exportSchema = false
)
abstract class CacheDatabase : RoomDatabase() {
    abstract fun messagesDao(): MessagesDAO
    abstract fun usersDao(): UsersDAO
    abstract fun channelsDao(): ChannelsDAO
    abstract fun topicsDao(): TopicsDAO
    abstract fun loginDAO(): LoginDAO
}