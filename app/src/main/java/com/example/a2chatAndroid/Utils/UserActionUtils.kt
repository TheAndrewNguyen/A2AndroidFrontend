package com.example.a2chatAndroid.Utils

import com.example.a2chatAndroid.Navigation.NavigationManager
import com.example.a2chatAndroid.Network.Firebase.authDeleteAndSignOut
import com.example.a2chatAndroid.Network.Firebase.authSignOutAndSignInAnonymously
import com.example.a2chatAndroid.Network.Firebase.createLobby
import com.example.a2chatAndroid.Network.Firebase.deleteLobby

//starting a chat
fun startChat() {
    authSignOutAndSignInAnonymously()
    createLobby()
    NavigationManager.navigateToChatScreen()
}

//Todo: app is crashing when we end a chat
//ending a chat
fun endChat() {
    deleteLobby()
    authDeleteAndSignOut()
    NavigationManager.navigateToHomeScreen()
}