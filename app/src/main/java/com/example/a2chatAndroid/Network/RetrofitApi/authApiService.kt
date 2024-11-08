package com.example.a2chatAndroid.Network.RetrofitApi

import android.util.Log
import com.example.a2chatAndroid.Network.RetrofitApi.RetroFitClient.apiService
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext


suspend fun authDeleteUser(uid: String) : Result<String> = withContext(coroutineContext) {
    Log.d("Retrofit", "authDeleteUser called: Trying to delete user: $uid")
    try {
        val response = apiService.authDeleteUser(uid)
        val data = response.body()

        if(response.isSuccessful) {
            Log.d("Retrofit", "User: $uid deleted successfully")
            return@withContext Result.success("User: $uid deleted successfully")
        } else {
            Log.w("Retrofit", "Error while trying to delete user $uid with error code ${response.code()} and message ${data?.message}")
            return@withContext Result.failure(Exception("Error while deleting user: ${data?.message}"))
        }
    } catch(e : Exception) {
        Log.w("Retrofit", "Error while trying to delete user $uid with exception: ${e.message}")
        return@withContext Result.failure(Exception("Error while trying to delete user: ${e.message}"))
    }
}