package com.example.a2chatAndroid.Network.RetrofitApi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT


interface BackEndApiService {
    @GET("/test/getcode")
    fun test() : Call<String> //returns a string

    //firestore endpoints
    @POST("/firestore/createLobby")
    fun createLobby() : Call<OnLobbyCreateResponse>

    @PUT
    fun addUserToLobby() : Call<OnLobbyCreateResponse>
}
