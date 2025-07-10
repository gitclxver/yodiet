package com.yodiet.ui.screens


import androidx.compose.material.icons.Icons
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.dietscreens.ui.theme.MealPlanTheme

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MealPlanTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ProfileScreen()
                }
            }
        }
    }
}
@Composable
fun ProfileScreen() {
    var username by remember { mutableStateOf("Username") }
    var usernameError by remember { mutableStateOf<String?>(null) }

    var fullName by remember { mutableStateOf("Full Name") }
    var fullNameError by remember { mutableStateOf<String?>(null) }

    var email by remember { mutableStateOf("example@gmail.com") }
    var emailError by remember { mutableStateOf<String?>(null) }

    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }

    var confirmPassword by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    fun validate(): Boolean {
        var valid = true

        if (username.isBlank()) {
            usernameError = "This field is required"
            valid = false
        } else {
            usernameError = null
        }

        if (fullName.isBlank()) {
            fullNameError = "This field is required"
            valid = false
        } else {
            fullNameError = null
        }

        if (email.isBlank()) {
            emailError = "This field is required"
            valid = false
        } else {
            emailError = null
        }

        if (password.isBlank()) {
            passwordError = "Password is required"
            valid = false
        } else if (password.length < 6) {
            passwordError = "Password must be at least 6 characters"
            valid = false
        } else {
            passwordError = null
        }

        if (confirmPassword != password) {
            confirmPasswordError = "Passwords don't match"
            valid = false
        } else {
            confirmPasswordError = null
        }

        return valid
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = "Profile Icon",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // The form fields aligned full width but Column is horizontally centered
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Profile", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    if (it.isNotBlank()) usernameError = null
                },
                label = { Text("Username") },
                isError = usernameError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (usernameError != null) {
                Text(usernameError!!, color = MaterialTheme.colorScheme.error)
            }

            OutlinedTextField(
                value = fullName,
                onValueChange = {
                    fullName = it
                    if (it.isNotBlank()) fullNameError = null
                },
                label = { Text("Full Name") },
                isError = fullNameError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (fullNameError != null) {
                Text(fullNameError!!, color = MaterialTheme.colorScheme.error)
            }

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (it.isNotBlank()) emailError = null
                },
                label = { Text("Email") },
                isError = emailError != null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            if (emailError != null) {
                Text(emailError!!, color = MaterialTheme.colorScheme.error)
            }

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (it.length >= 6) passwordError = null
                },
                label = { Text("Password") },
                isError = passwordError != null,
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            if (passwordError != null) {
                Text(passwordError!!, color = MaterialTheme.colorScheme.error)
            }

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    if (it == password) confirmPasswordError = null
                },
                label = { Text("Confirm Password") },
                isError = confirmPasswordError != null,
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            if (confirmPasswordError != null) {
                Text(confirmPasswordError!!, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    if (validate()) {
                        // TODO: Save the profile data (SharedPreferences, database, API, etc)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}
