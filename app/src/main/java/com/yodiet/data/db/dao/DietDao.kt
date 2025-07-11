package com.yodiet.data.db.dao

import androidx.room.*
import com.yodiet.data.db.model.Meal
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: Meal)

    @Update
    suspend fun updateMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("SELECT * FROM meals ORDER BY id DESC")
    fun getAllMeals(): Flow<List<Meal>>
}
