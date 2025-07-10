package com.yodiet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yodiet.ui.screens.HealthScreen
import com.yodiet.ui.theme.YoDietTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YoDietTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    YoDietApp()
                }
            }
        }
    }
}

@Composable
fun YoDietApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "health"
    ) {
        composable("health") {
            HealthScreen(
                onProfileClick = {
                    navController.navigate("profile")
                },
                onNavigateToGoals = {
                    navController.navigate("goals")
                },
                onNavigateToSettings = {
                    navController.navigate("settings")
                },
                onNavigateToStats = {
                    navController.navigate("stats")
                }
            )
        }

        // Placeholder screens for navigation
        composable("profile") {
            PlaceholderScreen(
                title = "Profile",
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("goals") {
            PlaceholderScreen(
                title = "Goals",
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("settings") {
            PlaceholderScreen(
                title = "Settings",
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("stats") {
            PlaceholderScreen(
                title = "Statistics",
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceholderScreen(
    title: String,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { androidx.compose.material3.Text(title) },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = onBackClick) {
                        androidx.compose.material3.Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            androidx.compose.material3.Text(
                text = "$title Screen\n(Coming Soon)",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}