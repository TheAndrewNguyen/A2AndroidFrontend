package com.example.a2chatAndroid.Network.RetrofitApi


data class OnLobbyCreateResponse (
    val code: String
)

data class OnLobbyJoinRequest(
    val lobbyID: String,
    val UID: String
)

data class OnLobbyJoinResponse(
    val message: String
)


