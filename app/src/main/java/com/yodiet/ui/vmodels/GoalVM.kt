package com.yodiet.ui.vmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yodiet.data.db.dao.GoalDao
import com.yodiet.data.db.dao.UserDao
import com.yodiet.data.db.model.Goal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(
    private val goalDao: GoalDao,
    private val userDao: UserDao
) : ViewModel() {

    val currentUser = userDao.getCurrentUserFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val allGoals: StateFlow<List<Goal>> = currentUser.flatMapLatest { user ->
        user?.let { goalDao.getGoalsByUserId(it.id) } ?: flowOf(emptyList())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun addGoal(goal: Goal) = viewModelScope.launch(Dispatchers.IO) {
        goalDao.insert(goal)
    }

    fun updateGoal(goal: Goal) = viewModelScope.launch(Dispatchers.IO) {
        goalDao.update(goal)
    }

    fun deleteGoal(goalId: Long) = viewModelScope.launch(Dispatchers.IO) {
        goalDao.delete(goalId)
    }
}
