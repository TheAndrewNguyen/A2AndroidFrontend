package com.example.a2chatAndroid.Network.RetrofitApi

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

//retrofit instance
object RetroFitClient {
    private const val BASE_URL = "http://ec2-18-117-87-87.us-east-2.compute.amazonaws.com:3000"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())  //converts object into readable
        .build()
}

