package com.alexpaxom.homework_2.domain.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexpaxom.homework_2.domain.entity.Topic

@Dao
interface TopicsDAO {

    @Query("SELECT * FROM Topics WHERE channel_id = :channelId")
    fun getTopicByChannelId(channelId: Int): List<Topic>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(topics: List<Topic>)

    @Query("DELETE FROM Topics")
    fun deleteAllTopics()
}