package com.yodiet.ui.vmodels

import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yodiet.data.db.dao.GoalDao
import com.yodiet.data.db.dao.UserDao
import com.yodiet.data.db.model.Goal
import com.yodiet.data.db.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileVM @Inject constructor(
    private val userDao: UserDao,
) : ViewModel() {

    val editableUsername = mutableStateOf("")
    val editableFirstName = mutableStateOf("")
    val editableLastName = mutableStateOf("")
    val editableEmail = mutableStateOf("")


    val isUsernameValid = mutableStateOf(true)
    val isFirstNameValid = mutableStateOf(true)
    val isEmailValid = mutableStateOf(true)


    private val _currentUser = mutableStateOf<User?>(null)
    val currentUser: State<User?> = _currentUser

    private val userId get() = _currentUser.value?.id

    sealed class ProfileResult {
        object Success : ProfileResult()
        data class Error(val message: String) : ProfileResult()
    }

    init {
        viewModelScope.launch {
            userDao.getCurrentUserFlow().collect { user ->
                _currentUser.value = user
                user?.let {
                    editableUsername.value = it.userName
                    editableFirstName.value = it.firstName
                    editableLastName.value = it.lastName
                    editableEmail.value = it.email
                }
            }
        }
    }

    suspend fun updateUserProfile(): ProfileResult {
        return try {
            when {
                editableUsername.value.isBlank() ->
                    ProfileResult.Error("Username cannot be empty")
                editableUsername.value.length < 4 ->
                    ProfileResult.Error("Username must be at least 4 characters")
                editableUsername.value.contains(" ") ->
                    ProfileResult.Error("Username cannot contain spaces")
                editableFirstName.value.isBlank() ->
                    ProfileResult.Error("First name cannot be empty")
                editableFirstName.value.length < 2 ->
                    ProfileResult.Error("First name must be at least 2 characters")
                editableEmail.value.isBlank() ->
                    ProfileResult.Error("Email cannot be empty")
                !Patterns.EMAIL_ADDRESS.matcher(editableEmail.value).matches() ->
                    ProfileResult.Error("Please enter a valid email")
                userId == null ->
                    ProfileResult.Error("User not found")
                else -> {
                    val existingUser = userDao.getUserById(userId!!)
                        ?: return ProfileResult.Error("User not found")

                    if (existingUser.userName != editableUsername.value &&
                        userDao.getUserByUsername(editableUsername.value) != null) {
                        return ProfileResult.Error("Username already taken")
                    }

                    if (existingUser.email != editableEmail.value &&
                        userDao.getUserByEmail(editableEmail.value) != null) {
                        return ProfileResult.Error("Email already in use")
                    }

                    val updatedUser = existingUser.copy(
                        userName = editableUsername.value,
                        firstName = editableFirstName.value,
                        lastName = editableLastName.value,
                        email = editableEmail.value
                    )

                    userDao.updateUser(updatedUser)
                    _currentUser.value = updatedUser
                    ProfileResult.Success
                }
            }
        } catch (e: Exception) {
            ProfileResult.Error("Update failed: ${e.message ?: "Unknown error"}")
        }
    }
}