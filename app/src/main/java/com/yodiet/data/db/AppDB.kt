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

@Database(
    entities = [
        User::class,
        Health::class,
        HealthProgress::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDB : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun healthDao(): HealthDao
//    abstract fun goalDao(): GoalDao
//    abstract fun DietDao(): DietDao

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
                    .fallbackToDestructiveMigration() // Optional: handles version changes by clearing DB
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}