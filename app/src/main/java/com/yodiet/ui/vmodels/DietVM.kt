package com.yodiet.ui.vmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yodiet.data.db.dao.MealDao
import com.yodiet.data.db.model.Meal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealViewModel @Inject constructor(
    private val mealDao: MealDao
) : ViewModel() {
    val allMeals: Flow<List<Meal>> = mealDao.getAllMeals()

    fun insertMeal(meal: Meal) = viewModelScope.launch {
        mealDao.insertMeal(meal)
    }

    fun updateMeal(meal: Meal) = viewModelScope.launch {
        mealDao.updateMeal(meal)
    }

    fun deleteMeal(meal: Meal) = viewModelScope.launch {
        mealDao.deleteMeal(meal)
    }
}