package com.yodiet.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNav(
    onProfileClick: () -> Unit,
    onNavigateToGoals: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToStats: () -> Unit
) {
    var showDropdown by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Button
                IconButton(onClick = onProfileClick) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = "Profile",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                // App Logo/Title
                Text(
                    text = "HealthApp",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )

                // Dropdown Menu
                Box {
                    IconButton(onClick = { showDropdown = true }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    DropdownMenu(
                        expanded = showDropdown,
                        onDismissRequest = { showDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Goals") },
                            onClick = {
                                showDropdown = false
                                onNavigateToGoals()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Statistics") },
                            onClick = {
                                showDropdown = false
                                onNavigateToStats()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            onClick = {
                                showDropdown = false
                                onNavigateToSettings()
                            }
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}