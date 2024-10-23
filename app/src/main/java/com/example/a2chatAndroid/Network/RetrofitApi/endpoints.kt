package com.example.a2chatAndroid.Network.RetrofitApi

import android.content.ClipData.Item
import retrofit2.Call
import retrofit2.http.GET

interface BackEndApiService {
    @GET("/test")
    fun test() : Call<String> //returns a string

    @GET("/getCode")
    fun getCode() : Call<List<Item>> //returns a string
}