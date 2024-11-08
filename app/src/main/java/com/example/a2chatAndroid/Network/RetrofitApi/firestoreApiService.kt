package com.example.a2chatAndroid.Network.RetrofitApi

import android.util.Log
import com.example.a2chatAndroid.Network.RetrofitApi.RetroFitClient.apiService
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext


//makes api request for /firestore/createLobby
suspend fun firestoreCreateLobby(): Result<String> {
    return try {
        Log.d("Retrofit", "Create Lobby called")
        val response = apiService.createLobby()

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
        val response = apiService.addUserToLobby(request)
        val data = response?.body()

        if (response.isSuccessful) {
            val message = data?.message
            if (message != null) {
                Log.d("Retrofit", "Response: $message")
                Result.success(data.message)
            } else {
                Log.w("Retrofit", "request failed with error code ${response.code()} and message ${data?.message}")
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

suspend fun firestoreRemoveUserFromLobby(lobbyCode: String, uid: String) : Result<String> = withContext(coroutineContext) {
    Log.d("Retrofit", "firestorRemoveFromLobby api called, Attemping to remove user $uid from lobby $lobbyCode")

    try {
        val response = apiService.removeUsersFromLobby(lobbyCode, uid)

        if(response.isSuccessful) {
            Log.d("Retrofit", "User $uid removed from lobby $lobbyCode succesfully")
            return@withContext Result.success("User $uid removed from lobby $lobbyCode succesfully")
        } else {
            return@withContext Result.failure(Exception("Error while removing user from lobby"))
        }
    } catch(e : Exception) {
        Log.w("Retrofit", "Error while removing user from lobby with exception: ${e.message}")
        return@withContext Result.failure(Exception("Error while removing user from lobby ${e.message}"))
    }
}