package com.yodiet.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    var name: String,
    var type: String,
    var description: String,
    var currentValue: Float,
    var targetValue: Float,
    var unit: String
) {
    val progress: Float
        get() = if (targetValue == 0f) 0f else (currentValue / targetValue).coerceIn(0f, 1f)
}

