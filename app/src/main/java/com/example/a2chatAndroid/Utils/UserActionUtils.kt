package com.example.a2chatAndroid.Utils

import com.example.a2chatAndroid.Network.CallBacks.LobbyManager
import com.example.a2chatAndroid.Network.RetrofitApi.firestoreCreateLobby

//starting a chat
fun startChat() {
    val lobbyManager = LobbyManager()
    firestoreCreateLobby(lobbyManager)
}

//Todo: app is crashing when we end a chat
//ending a chat
/*fun endChat() {
}*/