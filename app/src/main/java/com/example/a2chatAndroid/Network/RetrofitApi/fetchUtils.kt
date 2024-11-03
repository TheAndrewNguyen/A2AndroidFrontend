package com.example.a2chatAndroid.Network.RetrofitApi

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//example
fun fetchTestData() {
    val apiService = RetroFitClient.retrofit.create(BackEndApiService::class.java)
    apiService.test().enqueue(object : Callback<String> {  // Replace YourDataClass with the actual data class
        override fun onResponse(call: Call<String>, response: Response<String>) {
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    Log.d("Retrofit", "Response: $data")
                    // Handle the response data
                } else {
                    Log.w("Retrofit", "Response body is null")
                }
            } else {
                Log.w("Retrofit", "Response not successful: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<String>, t: Throwable) {
            Log.w("Retrofit", "Request failed", t)
        }
    })
}

//makes api request for /firestore/createLobby
suspend fun firestoreCreateLobby(): Result<String> {
    return try {
        Log.d("Retrofit", "Create Lobby called")
        val apiService = RetroFitClient.retrofit.create(BackEndApiService::class.java)
        val response = apiService.createLobby()

        //repsonse was succesful
        if (response.isSuccessful) {
            val data = response.body()
            val lobbyCode = data?.code
            if (lobbyCode != null) {
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
fun firestoreAddUserToLobby(lobbyCode : String, uid : String) {
    //get lobby code from masterLobbyManager
    Log.d("Retrofit", "User logged in: ${uid} with lobby Code: ${lobbyCode}")
}