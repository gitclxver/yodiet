package com.yodiet.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yodiet.ui.vmodels.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }
    var authError by remember { mutableStateOf<String?>(null) }

    val isEmailValid = email.isNotBlank() or email.contains("@")
    val isFirstNameValid = firstName.isNotBlank() && firstName.length >= 2
    val isLastNameValid = lastName.isNotBlank()
    val isUsernameValid = username.isNotBlank() && username.length >= 4 && !username.contains(" ")
    val isPasswordValid = password.isNotBlank() && password.length >= 6 && password.any { it.isDigit() }
    val doPasswordsMatch = password == confirmPassword && confirmPassword.isNotBlank()

    val showEmailError = isSubmitted && !isEmailValid
    val showFirstNameError = isSubmitted && !isFirstNameValid
    val showLastNameError = isSubmitted && !isLastNameValid
    val showUsernameError = isSubmitted && !isUsernameValid
    val showPasswordError = isSubmitted && !isPasswordValid
    val showConfirmPasswordError = isSubmitted && !doPasswordsMatch

    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email or Phone Number") },
            isError = showEmailError,
            supportingText = {
                if (showEmailError) {
                    Text("Valid email/phone required", color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                isError = showFirstNameError,
                supportingText = {
                    if (showFirstNameError) {
                        Text("Required", color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                isError = showLastNameError,
                supportingText = {
                    if (showLastNameError) {
                        Text("Required", color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            isError = showUsernameError,
            supportingText = {
                if (showUsernameError) {
                    Text(
                        when {
                            username.isBlank() -> "Required"
                            username.contains(" ") -> "No spaces allowed"
                            else -> "4+ characters"
                        },
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            isError = showPasswordError,
            supportingText = {
                if (showPasswordError) {
                    Text(
                        when {
                            password.isBlank() -> "Required"
                            password.length < 6 -> "Too short"
                            !password.any { it.isDigit() } -> "Needs a number"
                            else -> ""
                        },
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            isError = showConfirmPasswordError,
            supportingText = {
                if (showConfirmPasswordError) {
                    Text(
                        if (confirmPassword.isBlank()) "Required" else "Passwords don't match",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        authError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isSubmitted = true
                if (isEmailValid && isFirstNameValid && isLastNameValid &&
                    isUsernameValid && isPasswordValid && doPasswordsMatch) {
                    coroutineScope.launch {
                        isLoading = true
                        authError = null
                        when (val result = viewModel.signUp(
                            userName = username,
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            password = password
                        )) {
                            is AuthViewModel.AuthResult.Success -> {
                                isLoading = false
                                showSuccessDialog = true
                            }
                            is AuthViewModel.AuthResult.Error -> {
                                isLoading = false
                                authError = result.message
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isLoading && email.isNotBlank() && firstName.isNotBlank() &&
                    lastName.isNotBlank() && username.isNotBlank() &&
                    password.isNotBlank() && confirmPassword.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp))
            } else {
                Text("Sign Up")
            }
        }

        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = {
                    showSuccessDialog = false
                    navController.popBackStack()
                },
                title = { Text("Account Created") },
                text = { Text("Your account has been successfully created!") },
                confirmButton = {
                    Button(
                        onClick = {
                            showSuccessDialog = false
                            navController.popBackStack()
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }

        Text(
            text = "Already have an account? Log in",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable { navController.popBackStack() }
        )
    }
}

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}

