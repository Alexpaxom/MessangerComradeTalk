package com.alexpaxom.homework_2.domain.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexpaxom.homework_2.domain.entity.Channel

@Dao
interface ChannelsDAO {

    @Query("SELECT * FROM Channels WHERE in_subscribed = 1")
    fun getAllSubscribed(): List<Channel>

    @Query("SELECT * FROM Channels")
    fun getAll(): List<Channel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(channels: List<Channel>)

    @Query("DELETE FROM Channels WHERE in_subscribed = 1")
    fun deleteAllSubscribed()

    @Query("DELETE FROM Channels")
    fun deleteAllChannels()

    @Query("DELETE FROM Channels WHERE stream_id = :channelId")
    fun deleteChannel(channelId: Int)
}