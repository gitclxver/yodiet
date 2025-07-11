package com.yodiet.ui.vmodels

import androidx.lifecycle.ViewModel
import com.yodiet.data.db.dao.UserDao
import com.yodiet.data.db.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {

    sealed class AuthResult {
        object Success : AuthResult()
        data class Error(val message: String) : AuthResult()
    }

    suspend fun signUp(
        userName: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): AuthResult {
        return try {
            when {
                userDao.getUserByEmail(email) != null ->
                    AuthResult.Error("Email already registered")
                userDao.getUserByUsername(userName) != null ->
                    AuthResult.Error("Username already taken")
                password.length < 6 ->
                    AuthResult.Error("Password must be at least 6 characters")
                !password.any { it.isDigit() } ->
                    AuthResult.Error("Password must contain at least one number")
                else -> {
                    val user = User(
                        userName = userName,
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        password = password
                    )
                    userDao.insert(user)
                    AuthResult.Success
                }
            }
        } catch (e: Exception) {
            AuthResult.Error("Registration failed: ${e.message}")
        }
    }

    suspend fun login(
        emailOrUsername: String,
        password: String
    ): AuthResult {
        return try {
            userDao.clearCurrentUser()
            val user = userDao.getUserByEmailOrUsername(emailOrUsername)
            when {
                user == null -> AuthResult.Error("User not found")
                user.password != password -> AuthResult.Error("Incorrect password")
                else -> {
                    userDao.setCurrentUser(user.id)
                    AuthResult.Success
                }
            }
        } catch (e: Exception) {
            AuthResult.Error("Login failed: ${e.message}")
        }
    }

}