package com.example.a2chatAndroid.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class JoinScreenViewModel : ViewModel() {
    val LobbyViewModel = LobbyViewModel()

    fun navigateHome() {
        NavigationManager.navigateToHomeScreen()
    }

    fun joinChat(lobbyCode: String, calledFromCreateChatMethod: Boolean) {
        viewModelScope.launch {
            LobbyViewModel.joinChat(lobbyCode, false)
        }
    }
}