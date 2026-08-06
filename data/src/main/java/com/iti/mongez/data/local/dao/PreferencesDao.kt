package com.iti.mongez.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iti.mongez.data.local.entity.PreferencesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PreferencesDao {
    @JvmSuppressWildcards
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreferences(preferences: PreferencesEntity): Long

    @JvmSuppressWildcards
    @Query("SELECT * FROM user_preferences WHERE id = 0")
    suspend fun getPreferences(): PreferencesEntity?

}
