package com.example.a2chatAndroid.Network.RetrofitApi

import android.util.Log
import com.example.a2chatAndroid.Network.RetrofitApi.RetroFitClient.apiService


suspend fun authDeleteUser(uid: String) : Result<String> {
    return try {
        val requestBody = authDeleteUserRequest(uid)
        val response = apiService.authDeleteUser(requestBody)
        val data = response.body()

        if(response.isSuccessful) {
            return Result.success("User: $uid deleted successfully")
        } else {
            Log.w("Retrofit", "Error while trying to delete user $uid with error code ${response.code()} and message ${data?.message}")
            return Result.failure(Exception("Error while deleting user: ${data?.message}"))
        }
    } catch(e : Exception) {
        Log.w("Retrofit", "Error while trying to delete user $uid with exception: ${e.message}")
        return Result.failure(Exception("Error while trying to delete user: ${e.message}"))
    }
}