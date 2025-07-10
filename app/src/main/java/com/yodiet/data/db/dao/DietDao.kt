//package com.yodiet.data.db.dao
//
//import androidx.room.*
//
//import com.yodiet.data.db.model.Meal
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface MealDao {
//    @Insert
//    suspend fun insertMeal(meal: Meal): Long
//
//    @Update
//    suspend fun updateMeal(meal: Meal)
//
//    @Delete
//    suspend fun deleteMeal(meal: Meal)
//
//    @Query("SELECT * FROM meals ORDER BY title ASC")
//    fun getAllMeals(): Flow<List<Meal>>
//
//    @Query("SELECT * FROM meals WHERE id = :mealId")
//    suspend fun getMealById(mealId: Int): Meal?
//}