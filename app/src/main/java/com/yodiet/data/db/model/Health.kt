package com.yodiet.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "health_goals")
data class Health(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val targetValue: Int,
    val currentValue: Int = 0,
    val unit: String,
    val goalType: String,
    val createdDate: Date = Date(),
    val isCompleted: Boolean = false
)

@Entity(tableName = "health_progress")
data class HealthProgress(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val goalId: Int,
    val date: Date,
    val value: Int,
    val isCompleted: Boolean = false
)