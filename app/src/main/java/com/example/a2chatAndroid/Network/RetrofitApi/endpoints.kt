package com.example.a2chatAndroid.Network.RetrofitApi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST


interface BackEndApiService {
    @GET("/test/getcode")
    fun test() : Call<String> //returns a string

    //firestore endpoints
    @POST("/firestore/createLobby")
    fun createLobby() : Call<LobbyResponse>
}
