package com.yodiet.di

import android.content.Context
import androidx.room.Room
import com.yodiet.data.db.AppDB
import com.yodiet.data.db.dao.GoalDao
import com.yodiet.data.db.dao.HealthDao
import com.yodiet.data.db.dao.MealDao
import com.yodiet.data.db.dao.UserDao
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
    fun provideDatabase(@ApplicationContext context: Context): AppDB {
        return Room.databaseBuilder(
            context,
            AppDB::class.java,
            "app_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUserDao(db: AppDB): UserDao = db.userDao()

    @Provides
    fun provideHealthDao(db: AppDB): HealthDao = db.healthDao()

    @Provides
    fun provideMealDao(db: AppDB): MealDao = db.mealDao()

    @Provides
    fun provideGoalDao(db: AppDB): GoalDao = db.goalDao()
}
