package com.example.a2chatAndroid.Network.RetrofitApi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//retrofit instance
object RetroFitClient {
    private const val BASE_URL = "https://a2chat.mooo.com"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())  //converts object into readable
        .build()
}

