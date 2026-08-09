package com.iti.mongez.data.di


import android.content.Context
import androidx.room.Room
import com.iti.mongez.data.local.dao.PreferencesDao
import com.iti.mongez.data.local.dao.UserDao
import com.iti.mongez.data.local.db.MongezDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMongezDatabase(@ApplicationContext context: Context): MongezDatabase {
        return Room.databaseBuilder(
            context,
            MongezDatabase::class.java,
            "mongez_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: MongezDatabase): UserDao {
        return database.userDao
    }

    @Provides
    @Singleton
    fun providePreferencesDao(database: MongezDatabase): PreferencesDao {
        return database.preferencesDao
    }
}
