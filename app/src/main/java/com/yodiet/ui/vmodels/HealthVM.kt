package com.yodiet.ui.vmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yodiet.data.db.dao.HealthDao
import com.yodiet.data.db.dao.HealthProgressWithGoal
import com.yodiet.data.db.model.Health
import com.yodiet.data.db.model.HealthProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HealthVM @Inject constructor(
    private val healthDao: HealthDao
) : ViewModel() {

    // State flows
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Get daily goals
    val dailyGoals: Flow<List<Health>> = healthDao.getDailyGoals()

    // Get weekly progress
    val weeklyProgress: Flow<List<HealthProgressWithGoal>> = healthDao.getWeeklyProgressWithGoals(
        startDate = getStartOfWeek(),
        endDate = getEndOfWeek()
    )

    // Combined data for UI
    val healthData = combine(dailyGoals, weeklyProgress) { goals, progress ->
        HealthUiState(
            dailyGoals = goals,
            weeklyProgress = progress,
            isLoading = false
        )
    }

    fun updateGoalProgress(goalId: Int, newValue: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                healthDao.updateGoalProgress(goalId, newValue)

                // Also insert progress record
                val progress = HealthProgress(
                    goalId = goalId,
                    date = Date(),
                    value = newValue
                )
                healthDao.insertProgress(progress)

            } catch (e: Exception) {
                _error.value = "Failed to update progress: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addGoal(health: Health) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                healthDao.insertGoal(health)
            } catch (e: Exception) {
                _error.value = "Failed to add goal: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteGoal(health: Health) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                healthDao.deleteGoal(health)
            } catch (e: Exception) {
                _error.value = "Failed to delete goal: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    private fun getStartOfWeek(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    private fun getEndOfWeek(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.time
    }

    fun getProgressPercentage(current: Int, target: Int): Float {
        return if (target > 0) (current.toFloat() / target.toFloat()) * 100f else 0f
    }
}
data class HealthUiState(
    val dailyGoals: List<Health> = emptyList(),
    val weeklyProgress: List<HealthProgressWithGoal> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)