package com.alexpaxom.homework_2.domain.cache.daos

import androidx.room.*
import com.alexpaxom.homework_2.domain.entity.User

@Dao()
interface UsersDAO {
    @Query("SELECT * FROM Users WHERE userId = :userId")
    fun getUserById(userId: Int): User?

    @Query("SELECT * FROM Users  WHERE is_my_user = 1")
    fun getOwnUser(): User?

    @Query("SELECT * FROM Users")
    fun getAll(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(messages: List<User>)

    @Query("DELETE FROM Users")
    fun deleteAllUsers()
}