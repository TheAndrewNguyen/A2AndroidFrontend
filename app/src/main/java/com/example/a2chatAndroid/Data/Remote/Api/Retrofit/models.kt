package com.example.a2chatAndroid.Data.Remote.Api.Retrofit


//firestore data classes
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

//batch data classes
data class batchEndChatResponse(
    val message: String
)

//messaging data classes
data class sendMessageRequest(
    val userId: String,
    val messageContent: String
)

data class sendMessageResponse(
    val success: String,
    val message: String
)
