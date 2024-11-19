package com.example.a2chatAndroid.Network.Api

import com.example.a2chatAndroid.Network.Api.Retrofit.OnLobbyCreateResponse
import com.example.a2chatAndroid.Network.Api.Retrofit.OnLobbyJoinRequest
import com.example.a2chatAndroid.Network.Api.Retrofit.OnLobbyJoinResponse
import com.example.a2chatAndroid.Network.Api.Retrofit.batchEndChatResponse
import com.example.a2chatAndroid.Network.Api.Retrofit.sendMessageRequest
import com.example.a2chatAndroid.Network.Api.Retrofit.sendMessageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

//messaging imports
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
    @POST("/messages/{lobbyId}")
    suspend fun sendMessage(@Path("lobbyId") lobbyId: String, @Body requestBody: sendMessageRequest) : Response<sendMessageResponse>
}
