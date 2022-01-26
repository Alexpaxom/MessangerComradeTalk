package com.alexpaxom.homework_2.domain.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexpaxom.homework_2.domain.entity.LoginResult
import com.alexpaxom.homework_2.domain.entity.Topic

@Dao
interface LoginDAO {
    @Query("SELECT * FROM LoginParams")
    fun getLoginUserParams(): List<LoginResult>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(loginResult: LoginResult)

    @Query("DELETE FROM LoginParams")
    fun deleteAllLoginParams()
}