package com.yodiet.di

import android.content.Context
import com.yodiet.data.db.AppDB
import com.yodiet.data.db.dao.UserDao
import com.yodiet.data.db.dao.HealthDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Lives for the entire app lifecycle
object DBModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDB {
        return AppDB.getDatabase(context)
    }

    @Provides
    fun provideUserDao(database: AppDB): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideHealthDao(database: AppDB): HealthDao {
        return database.healthDao()
    }
}