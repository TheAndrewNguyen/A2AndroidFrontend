package com.example.a2chatAndroid.Utils

import com.example.a2chatAndroid.Firebase.authDeleteAndSignOut
import com.example.a2chatAndroid.Firebase.authSignOutAndSignInAnonymously
import com.example.a2chatAndroid.Firebase.createLobby
import com.example.a2chatAndroid.Firebase.deleteLobby
import com.example.a2chatAndroid.Navigation.NavigationManager

//starting a chat
fun startChat() {
    authSignOutAndSignInAnonymously()
    createLobby()
    NavigationManager.navigateToChatScreen()
}

//ending a chat
fun endChat() {
    deleteLobby()
    authDeleteAndSignOut()
    NavigationManager.navigateToHomeScreen()
}