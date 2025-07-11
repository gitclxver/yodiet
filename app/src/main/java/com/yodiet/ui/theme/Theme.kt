package com.yodiet.ui.theme

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6200EE),
    secondary = Color(0xFF03DAC6),
    tertiary = Color(0xFF3700B3),
    // Add other dark theme colors as needed
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),
    secondary = Color(0xFF03DAC6),
    tertiary = Color(0xFF3700B3),
    // Add other light theme colors as needed
)

@Composable
fun YoDietTheme(
    content: @Composable () -> Unit
) {
    val themeMode = ThemeManager.currentThemeState().value
    val systemInDarkTheme = isSystemInDarkTheme()

    val useDarkTheme = when (themeMode) {
        ThemeManager.THEME_LIGHT -> false
        ThemeManager.THEME_DARK -> true
        else -> systemInDarkTheme // Follow system when THEME_SYSTEM
    }

    val colorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        useDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

object ThemeManager {
    const val PREF_THEME = "app_theme"
    const val THEME_SYSTEM = 0
    const val THEME_LIGHT = 1
    const val THEME_DARK = 2

    @Composable
    fun currentThemeState(): State<Int> {
        val context = LocalContext.current
        var themeState by remember { mutableIntStateOf(getCurrentTheme(context)) }

        DisposableEffect(context) {
            val prefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
            val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == PREF_THEME) {
                    themeState = getCurrentTheme(context)
                }
            }
            prefs.registerOnSharedPreferenceChangeListener(listener)

            onDispose {
                prefs.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }

        return remember { mutableIntStateOf(themeState) }
    }

    private fun getCurrentTheme(context: Context): Int {
        return context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
            .getInt(PREF_THEME, THEME_SYSTEM)
    }

    fun setTheme(context: Context, theme: Int) {
        context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
            .edit(commit = true) {
                putInt(PREF_THEME, theme)
            }
    }
}