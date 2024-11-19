package com.example.a2chatAndroid.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel() {
    val LobbyViewModel = LobbyViewModel()

    fun navigateToJoinScreen() {
        NavigationManager.navigateToJoinScreen()
    }

    fun startChat() {
        viewModelScope.launch {
            LobbyViewModel.startChat()
        }
    }
}