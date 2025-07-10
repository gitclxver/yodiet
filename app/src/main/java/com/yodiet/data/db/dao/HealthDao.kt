package com.yodiet.data.db.dao

import androidx.room.*
import com.yodiet.data.db.model.Health
import com.yodiet.data.db.model.HealthProgress
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface HealthDao {

    @Query("SELECT * FROM health_goals ORDER BY createdDate DESC")
    fun getAllGoals(): Flow<List<Health>>

    @Query("SELECT * FROM health_goals WHERE goalType = 'daily' ORDER BY createdDate DESC")
    fun getDailyGoals(): Flow<List<Health>>

    @Query("SELECT * FROM health_goals WHERE id = :id")
    suspend fun getGoalById(id: Int): Health?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(health: Health)

    @Update
    suspend fun updateGoal(health: Health)

    @Delete
    suspend fun deleteGoal(health: Health)

    @Query("UPDATE health_goals SET currentValue = :value WHERE id = :id")
    suspend fun updateGoalProgress(id: Int, value: Int)

    @Query("SELECT * FROM health_progress WHERE goalId = :goalId ORDER BY date DESC")
    fun getProgressForGoal(goalId: Int): Flow<List<HealthProgress>>

    @Query("SELECT * FROM health_progress WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getProgressInDateRange(startDate: Date, endDate: Date): Flow<List<HealthProgress>>

    @Query("""
        SELECT hp.*, hg.title, hg.unit 
        FROM health_progress hp 
        JOIN health_goals hg ON hp.goalId = hg.id 
        WHERE hp.date >= :startDate AND hp.date <= :endDate 
        ORDER BY hp.date DESC
    """)
    fun getWeeklyProgressWithGoals(startDate: Date, endDate: Date): Flow<List<HealthProgressWithGoal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: HealthProgress)

    @Delete
    suspend fun deleteProgress(progress: HealthProgress)
}

data class HealthProgressWithGoal(
    val id: Int,
    val goalId: Int,
    val date: Date,
    val value: Int,
    val isCompleted: Boolean,
    val title: String,
    val unit: String
)