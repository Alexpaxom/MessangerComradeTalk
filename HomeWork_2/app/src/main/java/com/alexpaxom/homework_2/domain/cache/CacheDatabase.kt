package com.alexpaxom.homework_2.domain.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alexpaxom.homework_2.domain.cache.daos.MessagesDAO
import com.alexpaxom.homework_2.domain.cache.daos.UsersDAO
import com.alexpaxom.homework_2.domain.entity.Message
import com.alexpaxom.homework_2.domain.entity.User

@Database(entities = [Message::class, User::class], version = 2)
abstract class CacheDatabase: RoomDatabase() {
    abstract fun messagesDao(): MessagesDAO
    abstract fun usersDao(): UsersDAO
}