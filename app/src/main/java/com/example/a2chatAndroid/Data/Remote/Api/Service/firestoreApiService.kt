package com.example.a2chatAndroid.Data.RetrofitApi.Service

import android.util.Log
import com.example.a2chatAndroid.Data.Remote.Api.Retrofit.OnLobbyJoinRequest
import com.example.a2chatAndroid.Data.Remote.Api.Retrofit.RetroFitClient


//makes api request for /firestore/createLobby
suspend fun firestoreCreateLobby(): Result<String> {
    return try {
        Log.d("Retrofit", "Create Lobby called")
        val response = RetroFitClient.apiService.createLobby()

        //response was successful
        if (response.isSuccessful) {
            val data = response.body()
            val lobbyCode = data?.code
            if (lobbyCode != null) {
                Log.d("Retrofit", "Lobby crated succesfully")
                Result.success(lobbyCode)
            } else {
                Log.w("Retrofit", "Response body is null")
                Result.failure(Exception("Response body is null"))
            }

        } else {
            Log.w("Retrofit", "Response not successful: ${response.errorBody()?.string()}")
            Result.failure(Exception("Error while calling the api"))
        }

    } catch(e : Exception) {
        Log.w("Retrofit", "Request failed at network level with exception:", e)
        Result.failure(Exception("Request failed at the network level" + e.message))
    }
}


//makes api request for /firestore/addUserToLobby
suspend fun firestoreAddUserToLobby(uid : String, lobbyCode : String, ) : Result<String>{
    Log.d("Retrofit, firestoreAddUserToLobby", "firestoreAddUserToLobby Called with lobbyCode: ${lobbyCode} and uid: ${uid}")

    return try {
        val request = OnLobbyJoinRequest(lobbyCode, uid)
        val response = RetroFitClient.apiService.addUserToLobby(request)
        val data = response.body()

        if (response.isSuccessful) {
            val message = data?.message
            if (message != null) {
                Log.d("Retrofit", "Response: $message")
                Result.success(message)
            } else {
                Log.w("Retrofit", "request failed with error code ${response.code()} and message ${message}")
                Result.failure(Exception("Response body was null"))
            }
        } else {
            Log.w("Retrofit", "request failed while making request with error code ${response.code()} and message ${data?.message}, local error: ${response.errorBody()}")
            Result.failure(Exception("Request failed with code ${response.code()} reponse message: ${data?.message}"))
        }
    } catch(e : Exception) {
        Log.w("Retrofit", "Request failed at network level with exception:", e)
        Result.failure(Exception("Request failed at the network level" + e.message))
    }
}