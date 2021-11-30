package com.alexpaxom.homework_2.domain.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexpaxom.homework_2.domain.entity.Message

@Dao
interface MessagesDAO {
    @Query("SELECT * FROM Messages  WHERE stream_id = :streamId AND subject = :topicName ORDER BY id ASC")
    fun getAll(streamId: Int, topicName: String): List<Message>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(messages: List<Message>)

    @Query("DELETE FROM Messages WHERE stream_id = :streamId AND subject = :topicName")
    fun deleteTopicMessages(streamId: Int, topicName: String)
}