package com.iti.mongez.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iti.mongez.data.local.dao.PreferencesDao
import com.iti.mongez.data.local.dao.UserDao
import com.iti.mongez.data.local.entity.PreferencesEntity
import com.iti.mongez.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, PreferencesEntity::class],
    version = 3,
    exportSchema = false
)
abstract class MongezDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val preferencesDao: PreferencesDao
}
