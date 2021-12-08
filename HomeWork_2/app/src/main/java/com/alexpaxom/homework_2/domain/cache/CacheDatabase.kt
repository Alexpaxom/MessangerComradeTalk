package com.alexpaxom.homework_2.domain.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alexpaxom.homework_2.domain.cache.daos.ChannelsDAO
import com.alexpaxom.homework_2.domain.cache.daos.MessagesDAO
import com.alexpaxom.homework_2.domain.cache.daos.TopicsDAO
import com.alexpaxom.homework_2.domain.cache.daos.UsersDAO
import com.alexpaxom.homework_2.domain.entity.Channel
import com.alexpaxom.homework_2.domain.entity.Message
import com.alexpaxom.homework_2.domain.entity.Topic
import com.alexpaxom.homework_2.domain.entity.User

@Database(entities = [Message::class, User::class, Topic::class, Channel::class], version = 5)
abstract class CacheDatabase: RoomDatabase() {
    abstract fun messagesDao(): MessagesDAO
    abstract fun usersDao(): UsersDAO
    abstract fun channelsDao(): ChannelsDAO
    abstract fun topicsDao(): TopicsDAO
}