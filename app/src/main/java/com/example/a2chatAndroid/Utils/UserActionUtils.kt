package com.example.a2chatAndroid.Utils

import android.util.Log
import com.example.a2chatAndroid.Navigation.NavigationManager
import com.example.a2chatAndroid.Network.CallBacks.masterLobbyManager
import com.example.a2chatAndroid.Network.Firebase.authGetCurrentUser
import com.example.a2chatAndroid.Network.Firebase.authSignInAnonymously
import com.example.a2chatAndroid.Network.Firebase.authSignOut
import com.example.a2chatAndroid.Network.Firebase.safeSignOutandSignInAnonymously
import com.example.a2chatAndroid.Network.RetrofitApi.firestoreAddUserToLobby
import com.example.a2chatAndroid.Network.RetrofitApi.firestoreCreateLobby
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

//starting a chat
suspend fun startChat() {
    Log.d("Chat", "Start chat called")
    try {
        //tasks need to finish before addding user to lobby
        coroutineScope {
            //signing out
            val deferredTasks = listOf(
                async(Dispatchers.IO) {
                    Log.d("Chat", "Creating Lobby...")
                    firestoreCreateLobby()
                        .onSuccess { code ->
                            masterLobbyManager.onLobbyCreated(code)
                        }
                        .onFailure { error ->
                            Log.w("Chat", "Error while creating lobby with error code ", error)
                        }
               },
                //signing in anonymously
                async(Dispatchers.IO) {
                    Log.d("Chat", "Signing in Anonymously...")
                    safeSignOutandSignInAnonymously()
                        .onSuccess { message ->
                            Log.d("Chat", "User signed in with UID: ${message}")
                        }
                        .onFailure {
                            Log.w("Chat", "Error while signing in with error code ", it)
                        }
                }
            )
            deferredTasks.awaitAll()
        }


        //log calls
        Log.d("Chat", "User logged in after awaits: ${authGetCurrentUser()}")
        Log.d("Chat", "lobby code after awaits: ${masterLobbyManager.getStoredLobbyCode()}")

        //add user to lobby using the api call
        Log.d("Chat", "FireStoreAddUserToLobby called with uid: ${authGetCurrentUser()}, and lobby code ${masterLobbyManager.getStoredLobbyCode()}")
        firestoreAddUserToLobby(authGetCurrentUser().toString(),
            masterLobbyManager.getStoredLobbyCode().toString()
        )
            .onSuccess { message ->
                Log.d("Chat", "User added to lobby with message: $message")
            }
            .onFailure { error ->
                throw Error("Error while adding user to lobby with error code: $error")
                Log.w("Chat", "Error while adding user to lobby with error code: ", error)
            }

        if(authGetCurrentUser() == null || masterLobbyManager.getStoredLobbyCode() == null) {
            Log.w("Chat", "User or lobby code is null start chat failed")
            throw Error("User or lobby code is null")
        }

        //navigate to chatScreen
        Log.d("Chat", "Navigation called")
        NavigationManager.navigateToChatScreen()

        //end of start chat functionality
        Log.d("Chat", "Start chat succesful")

    } catch(e: Error) {
        Log.w("Chat", "Start chat failed with exception: $e")
    }
}

//function for when user joins a chat
suspend fun JoinChat(lobbyCode: String) {

    val uid = authSignInAnonymously()

    if(uid == null) {
        Log.w("Chat", "Could not join chat because UID is null")
        throw Error("User is null")
    }

    firestoreAddUserToLobby(uid.toString(), lobbyCode)
        .onSuccess {
            Log.d("Chat", "User: ${uid} joined lobby: ${lobbyCode} succesfully")
            masterLobbyManager.onLobbyCreated(lobbyCode)
        }
        .onFailure { error ->
            Log.w("Chat", "Failed to add user to lobby error: ${error}")
            throw Error("Failed to add user to lobby")
        }

    NavigationManager.navigateToChatScreen()
}

suspend fun endChat() {
    authSignOut()
    //delete the lobby with an api call and remove users??? //TODO: ask alex about this
    //navigate back to home screen
}

//TODO: implmeent error handling
fun onFailCreationOrLogin() {

}