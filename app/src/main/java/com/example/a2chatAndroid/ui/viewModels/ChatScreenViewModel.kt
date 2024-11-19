package com.example.a2chatAndroid.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ChatScreenViewModel : ViewModel() {

    val lobbyViewModel = LobbyViewModel()

     fun sendMessage(message: String) {
        viewModelScope.launch() {
            if (message == "") {
                return@launch
            } else {
                com.example.a2chatAndroid.Data.Remote.Api.Service.sendMessage(message) //send the message api
            }
        }
    }


    fun endChat() {
        viewModelScope.launch {
            lobbyViewModel.endChat()
        }
    }
}