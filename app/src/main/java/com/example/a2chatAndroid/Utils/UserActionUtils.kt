package com.example.a2chatAndroid.Utils

import android.util.Log
import com.example.a2chatAndroid.Navigation.NavigationManager
import com.example.a2chatAndroid.Network.CallBacks.masterLobbyManager
import com.example.a2chatAndroid.Network.Firebase.authGetCurrentUser
import com.example.a2chatAndroid.Network.Firebase.authSignOut
import com.example.a2chatAndroid.Network.Firebase.safeSignOutandSignInAnonymously
import com.example.a2chatAndroid.Network.RetrofitApi.authDeleteUser
import com.example.a2chatAndroid.Network.RetrofitApi.firestoreAddUserToLobby
import com.example.a2chatAndroid.Network.RetrofitApi.firestoreCreateLobby
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

//starting a chat
suspend fun startChat() {
    Log.d("Chat", "Starting a chat...")

    try {
        //tasks need to finish before addding user to lobby
        coroutineScope {
            //signing out
            val deferredTasks = listOf(
                async(Dispatchers.IO) {
                    Log.d("Chat", "Creating Lobby...")
                    firestoreCreateLobby()
                        .onSuccess { code ->
                            Log.d("Chat", "Lobby created with code: $code")
                            masterLobbyManager.onLobbyCreated(code)
                        }
                        .onFailure { error ->
                            Log.w("Chat", "Error while creating lobby with error: ", error)
                        }
                },
                //signing in anonymously
                async(Dispatchers.IO) {
                    Log.d("Chat", "Signing in Anonymously...")
                    safeSignOutandSignInAnonymously()
                        .onSuccess { message ->
                            Log.d("Chat", "User signed in succesfully with UID: ${message}")
                        }
                        .onFailure {
                            Log.w("Chat", "Error while signing in with error code ", it)
                        }
                }
            )
            deferredTasks.awaitAll()
        }

        //error handling
        if (authGetCurrentUser() == null || masterLobbyManager.getStoredLobbyCode() == null) {
            Log.w("Chat", "User or lobby code is null start chat failed")
            throw Error("User or lobby code is null")
        }

        //add user to lobby using the api call
        Log.d(
            "Chat",
            "JoinChat called current uid: ${authGetCurrentUser()}, and lobby code ${masterLobbyManager.getStoredLobbyCode()}"
        )
        JoinChat(masterLobbyManager.getStoredLobbyCode().toString(), true)

        //navigate to chatScreen
        Log.d("Chat", "Navigation called")
        NavigationManager.navigateToChatScreen()

        //end of start chat functionality
        Log.d("Chat", "Start chat succesful")

    } catch (e: Error) {
        Log.w("Chat", "Start chat failed with exception: $e")
    }
}

//function for when user joins a chat
suspend fun JoinChat(lobbyCode: String, calledFromCreateChatMethod: Boolean) {

    var uid = ""

    if (!calledFromCreateChatMethod) {
        safeSignOutandSignInAnonymously()
    }

    uid = authGetCurrentUser().toString()

    firestoreAddUserToLobby(uid.toString(), lobbyCode)
        .onSuccess {
            Log.d("Chat", "User: ${uid} joined lobby: ${lobbyCode} succesfully")
            if (!calledFromCreateChatMethod) masterLobbyManager.onLobbyCreated(lobbyCode)
        }
        .onFailure { error ->
            Log.w("Chat", "Failed to add user to lobby error: ${error}")
            throw Error("Failed to add user to lobby")
        }

    NavigationManager.navigateToChatScreen()
}

suspend fun endChat() {
    Log.d("Chat", "Ending chat...")
    val uid = authGetCurrentUser()

    Log.d("Chat", "Signing out user...")
    val signoutResult = authSignOut() //signing out user

    signoutResult.onSuccess {
        Log.d("Chat", "User $uid successfully signed out")
    }.onFailure { error ->
        Log.w("Chat", "Error while signing out user with error code: ", error)
    }

    Log.d("Chat", "Deleting user from auth directory...")
    val deleteResult = authDeleteUser(uid.toString())

    deleteResult.onSuccess {
        Log.d("Chat", "User $uid successfully deleted")
    }.onFailure { error ->
        Log.w("Chat", "Error while deleting user with error code: ", error)
    }

    //call an api to remove the user from the lobby


    //navigate back to home screen
    NavigationManager.navigateToHomeScreen()

}

//TODO: implmeent error handling
fun onFailCreationOrLogin() {

}