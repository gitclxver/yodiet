package com.yodiet.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yodiet.ui.components.EditProfileTopNav
import com.yodiet.ui.components.TopNav
import com.yodiet.ui.vmodels.ProfileVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    profileViewModel: ProfileVM
) {
    val currentUser by profileViewModel.currentUser.collectAsState()
    val showErrorDialog = remember { mutableStateOf(false) }

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
                OutlinedTextField(
                    value = profileViewModel.editableFirstName.value,
                    onValueChange = { profileViewModel.editableFirstName.value = it },
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !profileViewModel.isFirstNameValid.value,
                    supportingText = {
                        if (!profileViewModel.isFirstNameValid.value) {
                            Text("First name cannot be empty")
                        }
                    }
                )

                OutlinedTextField(
                    value = profileViewModel.editableLastName.value,
                    onValueChange = { profileViewModel.editableLastName.value = it },
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !profileViewModel.isLastNameValid.value,
                    supportingText = {
                        if (!profileViewModel.isLastNameValid.value) {
                            Text("Last name cannot be empty")
                        }
                    }
                )

                OutlinedTextField(
                    value = profileViewModel.editableEmail.value,
                    onValueChange = { profileViewModel.editableEmail.value = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !profileViewModel.isEmailValid.value,
                    supportingText = {
                        if (!profileViewModel.isEmailValid.value) {
                            Text("Please enter a valid email")
                        }
                    }
                )

                Button(
                    onClick = {
                        if (profileViewModel.updateUserProfile()) {
                            navController.popBackStack()
                        } else {
                            showErrorDialog.value = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Changes")
                }
            }
        }
    }

    if (showErrorDialog.value) {
        AlertDialog(
            onDismissRequest = { showErrorDialog.value = false },
            title = { Text("Validation Error") },
            text = { Text("Please check all fields and try again") },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog.value = false }
                ) {
                    Text("OK")
                }
            }
        )
    }
}