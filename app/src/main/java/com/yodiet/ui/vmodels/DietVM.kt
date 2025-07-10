//package com.yodiet.ui.vmodels
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.yodiet.data.db.model.Meal
//import com.yodiet.data.repo.MealRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class MealViewModel @Inject constructor(
//    private val repository: MealRepository
//) : ViewModel() {
//    val meals = repository.getAllMeals()
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = emptyList()
//        )
//
//    fun insertMeal(meal: Meal) = viewModelScope.launch {
//        repository.insertMeal(meal)
//    }
//
//    fun updateMeal(meal: Meal) = viewModelScope.launch {
//        repository.updateMeal(meal)
//    }
//
//    fun deleteMeal(meal: Meal) = viewModelScope.launch {
//        repository.deleteMeal(meal)
//    }
//}