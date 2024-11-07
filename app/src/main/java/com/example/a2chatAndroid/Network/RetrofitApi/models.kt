package com.example.a2chatAndroid.Network.RetrofitApi


data class OnLobbyCreateResponse (
    val code: String
)

data class OnLobbyJoinRequest(
    val lobbyId: String,
    val uid: String
)

data class OnLobbyJoinResponse(
    val message: String
)

data class authDeleteUserRequest(
    val uid: String
)


