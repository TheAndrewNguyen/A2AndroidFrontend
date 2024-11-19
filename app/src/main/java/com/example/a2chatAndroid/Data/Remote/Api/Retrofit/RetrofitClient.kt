package com.example.a2chatAndroid.Data.Remote.Api.Retrofit

import com.example.a2chatAndroid.Data.Remote.Api.BackEndApiService
import com.example.a2chatAndroid.Data.Remote.Api.okhttp.AuthInterceptor
import com.example.a2chatAndroid.Data.Repository.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//retrofit instance
object RetroFitClient {
    private const val BASE_URL = "https://a2chat.mooo.com"

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(TokenManager.getToken() ?: ""))
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())  //converts object into readable
        .build()

    val apiService = retrofit.create(BackEndApiService::class.java)
}

