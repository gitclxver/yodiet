package com.yodiet.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yodiet.data.db.dao.UserDao
import com.yodiet.data.db.model.User
import com.yodiet.data.db.model.Health
import com.yodiet.data.db.model.HealthProgress
import com.yodiet.data.db.dao.HealthDao
//import com.yodiet.data.db.dao.MealDao
//import com.yodiet.data.db.model.Meal

@Database(
    entities = [
        User::class,
        Health::class,
        HealthProgress::class,
        //Meal::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDB : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun healthDao(): HealthDao
//    abstract fun goalDao(): GoalDao
    //abstract fun mealDao(): MealDao

    companion object {
        @Volatile
        private var INSTANCE: AppDB? = null

        fun getDatabase(context: Context): AppDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDB::class.java,
                    "app_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}