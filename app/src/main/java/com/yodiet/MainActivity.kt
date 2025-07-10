package com.yodiet

import ProfileScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yodiet.nav.Routes
import com.yodiet.ui.screens.EditProfileScreen
import com.yodiet.ui.screens.HealthScreen
import com.yodiet.ui.screens.HomeScreen
import com.yodiet.ui.screens.SettingsScreen
import com.yodiet.ui.theme.ThemeManager
import com.yodiet.ui.theme.YoDietTheme
import com.yodiet.ui.vmodels.ProfileVM
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
    val darkTheme = ThemeManager.getCurrentTheme()

    val navController = rememberNavController()

    val profileVM: ProfileVM = hiltViewModel()

    YoDietTheme(darkTheme = darkTheme) {
        NavHost(
            navController = navController,
            startDestination = Routes.Home
        ) {
            composable(Routes.Home) {
                HomeScreen(navController = navController)
            }
            composable(Routes.Health) {
                HealthScreen(navController = navController)
            }
            composable(Routes.Profile) {
                ProfileScreen(navController = navController)
            }
            composable(Routes.EditProfile) {
                EditProfileScreen(
                    navController = navController,
                    profileViewModel = profileVM
                )
            }
            composable(Routes.Settings) {
                SettingsScreen(navController = navController)
            }
            // Add other routes as needed
        }
    }
}