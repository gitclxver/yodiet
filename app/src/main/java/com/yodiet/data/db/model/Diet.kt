package com.yodiet.data.db.model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class Meal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    @DrawableRes val imageRes: Int,
    val kcal: Int,
    val fat: Int,
    val carbs: Int,
    val protein: Int
)