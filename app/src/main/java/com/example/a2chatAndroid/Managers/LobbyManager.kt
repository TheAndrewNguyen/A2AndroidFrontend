package com.example.a2chatAndroid.Managers

import android.util.Log
import com.example.a2chatAndroid.Data.Api.Service.batchEndChat
import com.example.a2chatAndroid.Data.CallBacks.masterLobbyManager
import com.example.a2chatAndroid.Data.Firebase.authGetCurrentUser
import com.example.a2chatAndroid.Data.Firebase.authGetIdToken
import com.example.a2chatAndroid.Data.Firebase.authSignOut
import com.example.a2chatAndroid.Data.Firebase.safeSignOutandSignInAnonymously
import com.example.a2chatAndroid.Data.RetrofitApi.Service.firestoreAddUserToLobby
import com.example.a2chatAndroid.Data.RetrofitApi.Service.firestoreCreateLobby
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope


suspend fun authenticateAndAddtoken() {

    coroutineScope {
        async(Dispatchers.IO) {
            Log.d("Chat", "Signing in Anonymously...")
            safeSignOutandSignInAnonymously()
                .onSuccess { message ->
                    Log.d("Chat", "User signed in succesfully with UID: ${message}")
                    TokenManager.setToken(authGetIdToken().toString())
                }
                .onFailure {
                    Log.w("Chat", "Error while signing in with error code ", it)
                }
        }
    }
}


//starting a chat
suspend fun startChat() {
    Log.d("Chat", "Starting a chat...")

    try {
        Log.d("Chat", "Authenticating user...")
        authenticateAndAddtoken()

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
    try {
        var uid = ""

        if (!calledFromCreateChatMethod) {
           authenticateAndAddtoken()
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
    } catch (error: Error) {
        Log.w("Chat", "An error occured while trying to join the chat: ${error}")
    }
}

suspend fun endChat() {
    try {
        Log.d("Chat", "Ending chat...")
        val current_uid = authGetCurrentUser()

        coroutineScope {
            val deleteUserAndLobbyTask = async {
                Log.d("Chat", "Deleting user and lobby...")
                val deleteUserAndLobbyResult = batchEndChat(
                    masterLobbyManager.getStoredLobbyCode().toString(),
                    current_uid.toString()
                )
                deleteUserAndLobbyResult.onSuccess {
                    Log.d("Chat", "User and lobby successfully deleted")
                }.onFailure { error ->
                    Log.w("Chat", "Error while deleting user and lobby with error code: ", error)
                }
                deleteUserAndLobbyResult
            }

            //await for the asks to finish
            deleteUserAndLobbyTask.await()

            Log.d("Chat", "All end chat tasks completed")
        }

        coroutineScope {
            //sign out user locally
            val signOutTask = async {
                Log.d("Chat", "Signing out user...")
                val signOutResult = authSignOut() //signing out user
                signOutResult.onSuccess {
                    Log.d("Chat", "User $current_uid successfully signed out")
                }.onFailure { error ->
                    Log.w("Chat", "Error while signing out user with error code: ", error)
                }
                signOutResult
            }
            signOutTask.await()
        }


        //navigate back to home screen
        Log.d("Chat", "Navigating back to home screen")
        NavigationManager.navigateToHomeScreen()
    } catch (error: Error) {
        Log.w("Chat", "An error occured while trying to end the chat: ${error}")
    }
}

//TODO: implmeent error handling
fun onFailCreationOrLogin() {

}