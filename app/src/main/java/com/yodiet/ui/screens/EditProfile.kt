package com.yodiet.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yodiet.ui.components.EditProfileTopNav
import com.yodiet.ui.vmodels.ProfileVM
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    profileViewModel: ProfileVM = hiltViewModel(),
) {
    val currentUser = profileViewModel.currentUser.value
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            EditProfileTopNav(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (currentUser == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Username field
                OutlinedTextField(
                    value = profileViewModel.editableUsername.value,
                    onValueChange = { profileViewModel.editableUsername.value = it },
                    label = { Text("Username*") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !profileViewModel.isUsernameValid.value,
                    supportingText = {
                        if (!profileViewModel.isUsernameValid.value) {
                            Text(
                                "Minimum 4 characters, no spaces",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )

                OutlinedTextField(
                    value = profileViewModel.editableFirstName.value,
                    onValueChange = { profileViewModel.editableFirstName.value = it },
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !profileViewModel.isFirstNameValid.value,
                    supportingText = {
                        if (!profileViewModel.isFirstNameValid.value) {
                            Text(
                                "Minimum 2 characters",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )

                OutlinedTextField(
                    value = profileViewModel.editableLastName.value,
                    onValueChange = { profileViewModel.editableLastName.value = it },
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth()
                )


                OutlinedTextField(
                    value = profileViewModel.editableEmail.value,
                    onValueChange = { profileViewModel.editableEmail.value = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !profileViewModel.isEmailValid.value,
                    supportingText = {
                        if (!profileViewModel.isEmailValid.value) {
                            Text(
                                "Valid email required",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )


                Button(
                    onClick = {
                        isLoading = true
                        coroutineScope.launch {
                            when (val result = profileViewModel.updateUserProfile()) {
                                is ProfileVM.ProfileResult.Success -> {
                                    navController.popBackStack()
                                }
                                is ProfileVM.ProfileResult.Error -> {
                                    errorMessage = result.message
                                    showErrorDialog = true
                                }
                            }
                            isLoading = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text("Save Changes")
                    }
                }
            }
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false }
                ) {
                    Text("OK")
                }
            }
        )
    }
}