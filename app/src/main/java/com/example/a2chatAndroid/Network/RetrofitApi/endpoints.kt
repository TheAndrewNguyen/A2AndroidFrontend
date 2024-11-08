package com.example.a2chatAndroid.Network.RetrofitApi

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface BackEndApiService {
    @GET("/test/getcode")
    fun test() : Call<String> //returns a string

    //firestore endpoints
    @POST("/firestore/createLobby")
    suspend fun createLobby() : Response<OnLobbyCreateResponse>

    @PUT("/firestore/addUserToLobby")
    suspend fun addUserToLobby(@Body requestBody: OnLobbyJoinRequest): Response<OnLobbyJoinResponse>

    @DELETE("/firestore/removeUsersFromLobby/{lobbyId}/{uid}")
    suspend fun removeUsersFromLobby(@Path("lobbyId") lobbyId: String, @Path("uid") uid: String) : Response<authDeleteUserResponse>

    @DELETE("/auth/deleteUser/{uid}")
    suspend fun authDeleteUser(@Path("uid") uid: String) : Response<authDeleteUserResponse>
}
