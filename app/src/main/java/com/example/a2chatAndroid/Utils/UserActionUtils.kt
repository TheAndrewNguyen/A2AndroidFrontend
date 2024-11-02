package com.example.a2chatAndroid.Utils

import android.util.Log
import com.example.a2chatAndroid.Network.CallBacks.LobbyManager
import com.example.a2chatAndroid.Network.Firebase.authSignInAnonymously
import com.example.a2chatAndroid.Network.RetrofitApi.firestoreCreateLobby

val masterLobbyManager = LobbyManager()

//starting a chat
fun startChat() {
    authSignInAnonymously() //sign in the user
    Log.d("Chat", "Start chat called")
    firestoreCreateLobby(masterLobbyManager) //call the api to create a lobby
    //all the api to add the user to the lobby
}

//ending a chat
fun endChat() {
    //sign out user
    //delete all users from lobby using the api call
    //navigate back to home screen
}