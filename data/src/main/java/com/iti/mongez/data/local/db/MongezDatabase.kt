package com.iti.mongez.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iti.mongez.data.local.dao.UserDao
import com.iti.mongez.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 2,
    exportSchema = false
)
abstract class MongezDatabase : RoomDatabase() {
    abstract val userDao: UserDao
}
