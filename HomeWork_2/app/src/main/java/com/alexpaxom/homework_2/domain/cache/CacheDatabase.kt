package com.alexpaxom.homework_2.domain.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alexpaxom.homework_2.domain.cache.daos.MessagesDAO
import com.alexpaxom.homework_2.domain.entity.Message

@Database(entities = [Message::class], version = 1)
abstract class CacheDatabase: RoomDatabase() {
    abstract fun messagesDao(): MessagesDAO
}