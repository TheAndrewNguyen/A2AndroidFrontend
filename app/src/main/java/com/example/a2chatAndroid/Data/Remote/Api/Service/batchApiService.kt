package com.example.a2chatAndroid.Data.Remote.Api.Service

import android.util.Log
import com.example.a2chatAndroid.Data.Remote.Api.Retrofit.RetroFitClient
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext


suspend fun batchEndChat(lobbyId: String, uid: String) :  Result<String> = withContext(coroutineContext) {
    Log.d("Retrofit", "batchEndChat called with lobbyId: $lobbyId and uid: $uid")
    try {
        val response = RetroFitClient.apiService.batchEndChat(lobbyId, uid)
        if(response.isSuccessful){
            return@withContext Result.success("Chat ended successfully")
        } else {
            return@withContext Result.failure(Exception("Error while trying to end chat"))
        }
    }catch(e : Exception) {
        return@withContext Result.failure(Exception("Error while trying to end chat: ${e.message}"))
    }
}

