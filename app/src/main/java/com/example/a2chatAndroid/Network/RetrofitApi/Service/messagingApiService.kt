package com.example.a2chatAndroid.Network.RetrofitApi.Service

import android.util.Log
import com.example.a2chatAndroid.Network.Firebase.authGetCurrentUser
import com.example.a2chatAndroid.Network.RetrofitApi.RetroFitClient
import com.example.a2chatAndroid.Network.RetrofitApi.sendMessageRequest
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

suspend fun sendMessage(lobbyId: String, messageContent: String) : Result<String> = withContext(coroutineContext) {
    Log.d("Retrofit", "sendMessage called with lobbyId: $lobbyId and messageContent: $messageContent")
    try {
        val response = RetroFitClient.apiService.sendMessage(lobbyId, sendMessageRequest(authGetCurrentUser()!!, messageContent))
        if(response.isSuccessful) {
            return@withContext Result.success("Message sent successfully")
        } else {
            return@withContext Result.failure(Exception("Error while trying to send message during the api call ${response.message()}"))
        }
    } catch(error : Exception) {
     return@withContext Result.failure(Exception("Error while trying to send message: ${error.message}"))
    }
}