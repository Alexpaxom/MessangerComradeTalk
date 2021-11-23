package com.alexpaxom.homework_2.domain.cache

import androidx.room.Room
import com.alexpaxom.homework_2.app.App

// временное решение потом будет переделано на Dagger или что-то другое
object GetDatabaseObject {
    val db = Room.databaseBuilder(
        App.instance,
        CacheDatabase::class.java, "database-name"
    ).build()

    val messagesDao = db.messagesDao()
    val usersDao = db.usersDao()
}