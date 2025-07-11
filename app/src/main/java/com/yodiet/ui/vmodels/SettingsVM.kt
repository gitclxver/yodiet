package com.yodiet.ui.vmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yodiet.data.db.dao.UserDao
import com.yodiet.ui.theme.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userDao: UserDao,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    private val currentUser = userDao.getCurrentUserFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun getCurrentTheme(): Int {
        val prefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        return prefs.getInt(ThemeManager.PREF_THEME, ThemeManager.THEME_SYSTEM)
    }

    fun setTheme(theme: Int) {
        viewModelScope.launch {
            ThemeManager.setTheme(context, theme)
            _uiEvents.send(UiEvent.ThemeChanged)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            currentUser.value?.let { user ->
                userDao.clearCurrentUser()
                _uiEvents.send(UiEvent.NavigateToLogin)
                _uiEvents.send(UiEvent.ShowToast("Signed out successfully"))
            } ?: run {
                _uiEvents.send(UiEvent.ShowToast("No user logged in"))
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            currentUser.value?.let { user ->
                userDao.delete(user)
                _uiEvents.send(UiEvent.NavigateToLogin)
                _uiEvents.send(UiEvent.ShowToast("Account deleted"))
            } ?: run {
                _uiEvents.send(UiEvent.ShowToast("No user logged in"))
            }
        }
    }

    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
        object NavigateToLogin : UiEvent()
        object ThemeChanged : UiEvent()
    }
}