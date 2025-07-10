package com.yodiet.data.db.model

data class Goal(
    val id: Int,
    var name: String,
    var type: String,
    var description: String,
    var currentValue: Float,
    var targetValue: Float,
) {
    val progress: Float
        get() = if (targetValue == 0f) 0f else (currentValue / targetValue).coerceIn(0f, 1f)
}

