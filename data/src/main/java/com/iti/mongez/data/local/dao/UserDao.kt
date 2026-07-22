package com.iti.mongez.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iti.mongez.data.local.entity.UserEntity

@Dao
interface UserDao {
    @JvmSuppressWildcards
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @JvmSuppressWildcards
    @Query("SELECT * FROM `users` LIMIT 1")
    suspend fun getUser(): UserEntity?

    @JvmSuppressWildcards
    @Query("DELETE FROM `users`")
    suspend fun clearUser(): Int
}
