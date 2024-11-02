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
    private var storedErrorMessage: String? = null

    override fun onLobbyCreated(lobbyCode: String) {
        storedLobbyCode = lobbyCode
        Log.d("Lobby Manager", "onLobbyCreated called, Lobby created with code: from get stored lobby code " + getStoredLobbyCode())

        NavigationManager.navigateToChatScreen()
    }

    override fun onLobbyCreatedError(errorMessage: String) {
        storedErrorMessage = errorMessage
    }

    fun getStoredLobbyCode() : String? {
        return storedLobbyCode
    }

    fun getStoredErrorMessage() : String? {
        return storedErrorMessage
    }
}
