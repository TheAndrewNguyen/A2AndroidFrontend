package com.example.a2chatAndroid.Network.RetrofitApi

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface BackEndApiService {
    //firestore endpoints
    @POST("/firestore/createLobby")
    suspend fun createLobby() : Response<OnLobbyCreateResponse>

    @PUT("/firestore/addUserToLobby")
    suspend fun addUserToLobby(@Body requestBody: OnLobbyJoinRequest): Response<OnLobbyJoinResponse>

    //batch endpoints
    @DELETE("/batch/endChat/{lobbyId}/{uid}")
    suspend fun batchEndChat(@Path("lobbyId") lobbyId: String, @Path("uid") uid: String) : Response<batchEndChatResponse>

    //messaging endpoints
}
