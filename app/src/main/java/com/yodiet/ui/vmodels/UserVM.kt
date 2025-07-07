package com.yodiet.ui.vmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yodiet.data.db.dao.UserDao
import com.yodiet.data.db.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {

    val allUsers = userDao.getAllUsers()

    fun insert(user: User) = viewModelScope.launch {
        userDao.insert(user)
    }

    fun delete(user: User) = viewModelScope.launch {
        userDao.delete(user)
    }
}