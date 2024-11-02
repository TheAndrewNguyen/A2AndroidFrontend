package com.example.a2chatAndroid.Network.CallBacks

import android.util.Log
import com.example.a2chatAndroid.Navigation.NavigationManager

//get the lobby code from createLobby
interface LobbyResponseCallback {
    fun onLobbyCreated(lobbyCode: String)
    fun onLobbyCreatedError(errorMessage: String)
}

class LobbyManager : LobbyResponseCallback {
    private var storedLobbyCode : String? = null
    private var storedErrorMessage : String? = null

    override fun onLobbyCreated(lobbyCode: String) {
        storedLobbyCode = lobbyCode
        Log.d("Lobby Manager", "Lobby created with code: $lobbyCode")
        val test = getStoredLobbyCode()
        Log.d("Lobby Manager", "Test call code:${test}")
        //navigate to create screen
        NavigationManager.navigateToChatScreen()
    }

    override fun onLobbyCreatedError(errorMessage: String) {
        storedErrorMessage = errorMessage
    }

    fun getStoredLobbyCode() : String? {
        Log.d("Lobby Manager", "getStoredLobbyCode called, current code: $storedLobbyCode")
        return storedLobbyCode
    }

    fun getStoredErrorMessage() : String? {
        return storedErrorMessage
    }
}
