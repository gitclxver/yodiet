package com.yodiet.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yodiet.data.db.dao.*
import com.yodiet.data.db.model.*

@Database(
    entities = [
        User::class,
        Health::class,
        HealthProgress::class,
        Meal::class,
        Goal::class
    ],
    version = 6,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDB : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun healthDao(): HealthDao
    abstract fun mealDao(): MealDao
    abstract fun goalDao(): GoalDao
}
