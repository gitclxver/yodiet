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
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.yodiet.nav.Routes
import com.yodiet.ui.screens.*
import com.yodiet.ui.theme.YoDietTheme
import com.yodiet.ui.vmodels.ProfileVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YoDietApp()
        }
    }
}

@Composable
fun YoDietApp() {
    YoDietTheme {
        val systemUiController = rememberSystemUiController()
        val colorScheme = MaterialTheme.colorScheme
        val navController = rememberNavController()
        val profileVM: ProfileVM = hiltViewModel()

        val isDarkTheme = colorScheme.surface.luminance() < 0.5f

        SideEffect {
            systemUiController.apply {
                setStatusBarColor(
                    color = colorScheme.surface,
                    darkIcons = !isDarkTheme
                )
                setNavigationBarColor(
                    color = colorScheme.surface,
                    darkIcons = !isDarkTheme
                )
            }
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = Routes.LoginScreen
            ) {
                composable(Routes.LoginScreen) {
                    LoginScreen(
                        navController = navController
                    )
                }
                composable(Routes.SignUpScreen) {
                    SignUpScreen(
                        navController = navController
                    )
                }
                composable(Routes.HomeScreen) { HomeScreen(navController) }
                composable(Routes.HealthScreen) { HealthScreen(navController) }
                composable(Routes.ProfileScreen) { ProfileScreen(navController) }
                composable(Routes.GoalsScreeen) {ProfileScreen(navController)}
                composable(Routes.DietScreen) {DietScreen(navController)}
                composable(Routes.EditProfile) { EditProfileScreen(navController, profileVM) }
                composable(Routes.SettingsScreen) { SettingsScreen(navController) }
            }
        }
    }
}