package com.example.a2chatAndroid.Network.RetrofitApi

import android.util.Log
import com.example.a2chatAndroid.Network.CallBacks.LobbyResponseCallback
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
fun firestoreCreateLobby(callback : LobbyResponseCallback) {
    Log.d("Retrofit", "Create Lobby called")
    val apiService = RetroFitClient.retrofit.create(BackEndApiService::class.java)
    apiService.createLobby().enqueue(object : Callback<LobbyResponse> {
        override fun onResponse(call: Call<LobbyResponse>, response: Response<LobbyResponse>) {
            if (response.isSuccessful) { //succesful call
                val data = response.body()
                if (data != null) {
                    Log.d("Retrofit", "Lobby crated succesfuly with lobby code: ${data.code}")
                    callback.onLobbyCreated(data.code)
                } else {
                    Log.w("Retrofit", "Response body is null")
                }
            } else { //failure at the response level
                val error_message = response.errorBody()?.string()
                callback.onLobbyCreatedError(error_message ?: "Unknown error")
                Log.w("Retrofit", "Response not successful: ${error_message}")
            }
        }

        //failure at the network level
        override fun onFailure(call: Call<LobbyResponse>, t: Throwable) {
            callback.onLobbyCreatedError("Request failed at the network level" + t.message)
            Log.w("Retrofit", "Request failed at the network level", t)
        }
    })
}