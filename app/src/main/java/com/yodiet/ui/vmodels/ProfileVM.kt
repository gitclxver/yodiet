package com.yodiet.ui.vmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yodiet.data.db.dao.UserDao
import com.yodiet.data.db.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileVM @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {

    private var userId: Long? = null

    // Editable form fields
    val editableFirstName = mutableStateOf("")
    val editableLastName = mutableStateOf("")
    val editableEmail = mutableStateOf("")

    // Validation states
    val isFirstNameValid = mutableStateOf(true)
    val isLastNameValid = mutableStateOf(true)
    val isEmailValid = mutableStateOf(true)

    // Live observed user data
    val currentUser = userDao.getCurrentUserFlow()
        .map { user ->
            user?.let {
                userId = it.id // Set the userId when user is loaded
                editableFirstName.value = it.firstName
                editableLastName.value = it.lastName
                editableEmail.value = it.email
            }
            user
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Update user in Room
    fun updateUserProfile(): Boolean {
        val firstNameValid = editableFirstName.value.isNotBlank()
        val lastNameValid = editableLastName.value.isNotBlank()
        val emailValid = editableEmail.value.isNotBlank() &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(editableEmail.value).matches()

        isFirstNameValid.value = firstNameValid
        isLastNameValid.value = lastNameValid
        isEmailValid.value = emailValid

        if (firstNameValid && lastNameValid && emailValid) {
            viewModelScope.launch {
                userId?.let { id ->
                    val user = userDao.getUserById(id)
                    if (user != null) {
                        val updatedUser = user.copy(
                            firstName = editableFirstName.value,
                            lastName = editableLastName.value,
                            email = editableEmail.value
                        )
                        userDao.updateUser(updatedUser)
                    }
                }
            }
            return true
        }
        return false
    }
}