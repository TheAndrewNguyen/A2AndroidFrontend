package com.example.a2chatAndroid.Data.Remote.Api.Service

import android.util.Log
import com.example.a2chatAndroid.Data.Remote.Api.Retrofit.RetroFitClient
import com.example.a2chatAndroid.Data.Remote.Api.Retrofit.sendMessageRequest
import com.example.a2chatAndroid.Data.Remote.Firebase.authGetCurrentUser
import com.example.a2chatAndroid.Data.Repository.masterLobbyManager
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

suspend fun sendMessage(messageContent: String) : Result<String> = withContext(coroutineContext) {
    Log.d("Retrofit", "sendMessage called with lobbyId: ${masterLobbyManager.getStoredLobbyCode()} and messageContent: $messageContent")
    try {
        val response = RetroFitClient.apiService.sendMessage(
            masterLobbyManager.getStoredLobbyCode().toString(),
            sendMessageRequest(authGetCurrentUser()!!, messageContent)
        )
        if(response.isSuccessful) {
            return@withContext Result.success("Message sent successfully")
        } else {
            Log.w("Retrofit", "Error while trying to send message: ${response.message()}")
            return@withContext Result.failure(Exception("Error while trying to send message during the api call ${response.message()}"))
        }
    } catch(error : Exception) {
        Log.w("Retrofit", "Error while trying to send message: ${error.message}")
        return@withContext Result.failure(Exception("Error while trying to send message: ${error.message}"))
    }
}