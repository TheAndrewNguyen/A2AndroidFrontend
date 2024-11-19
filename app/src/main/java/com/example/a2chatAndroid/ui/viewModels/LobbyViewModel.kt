package com.example.a2chatAndroid.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2chatAndroid.Data.Remote.Api.Service.batchEndChat
import com.example.a2chatAndroid.Data.Remote.Firebase.authGetCurrentUser
import com.example.a2chatAndroid.Data.Remote.Firebase.authGetIdToken
import com.example.a2chatAndroid.Data.Remote.Firebase.authSignOut
import com.example.a2chatAndroid.Data.Remote.Firebase.safeSignOutandSignInAnonymously
import com.example.a2chatAndroid.Data.Repository.TokenManager
import com.example.a2chatAndroid.Data.Repository.masterLobbyManager
import com.example.a2chatAndroid.Data.RetrofitApi.Service.firestoreAddUserToLobby
import com.example.a2chatAndroid.Data.RetrofitApi.Service.firestoreCreateLobby
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class LobbyViewModel : ViewModel() {
    suspend fun authenticateAndAddtoken() {
        try {
            Log.d("Chat", "Signing in Anonymously...")
            safeSignOutandSignInAnonymously() // Sign in anonymously
                .onSuccess { message ->
                    Log.d("Chat", "User signed in succesfully with UID: ${message}")
                    TokenManager.setToken(authGetIdToken().toString())
                }
                .onFailure {
                    Log.w("Chat", "Error while signing in with error code ", it)
                }
        } catch (e: Exception) {
            Log.w("Chat", "An error occured while trying to sign the user in: ${e}")
        }
    }

    //starting a chat
    suspend fun startChat() {
        Log.d("Chat", "Starting a chat...")

        try {
            Log.d("Chat", "Authenticating user...")
            authenticateAndAddtoken()

            //tasks need to finish before addding user to lobby
            viewModelScope.launch {
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
                )
                deferredTasks.awaitAll()


                //error handling
                if (authGetCurrentUser() == null || masterLobbyManager.getStoredLobbyCode() == null) {
                    Log.w(
                        "Chat",
                        "User or lobby code is null start chat failed ${authGetCurrentUser()}, ${masterLobbyManager.getStoredLobbyCode()}"
                    )
                    throw Error("User or lobby code is null")
                }

                //add user to lobby using the api call
                Log.d(
                    "Chat",
                    "JoinChat called current uid: ${authGetCurrentUser()}, and lobby code ${masterLobbyManager.getStoredLobbyCode()}"
                )

                joinChat(masterLobbyManager.getStoredLobbyCode().toString(), true)

                //navigate to chatScreen
                Log.d("Chat", "Navigation called")
                NavigationManager.navigateToChatScreen()

                //end of start chat functionality
                Log.d("Chat", "Start chat succesful")
            }
        } catch (e: Error) {
            Log.w("Chat", "Start chat failed with exception: $e")
        }
    }

    //function for when user joins a chat
    suspend fun joinChat(lobbyCode: String, calledFromCreateChatMethod: Boolean) {
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

            NavigationManager.navigateToChatScreen() //navigate to the chat screen
        } catch (error: Error) {
            Log.w("Chat", "An error occured while trying to join the chat: ${error}")
        }
    }

    suspend fun endChat() {
        try {
            Log.d("Chat", "Ending chat...")
            val current_uid = authGetCurrentUser()

            // ends that chat
            viewModelScope.launch {
                val deleteUserAndLobbyTask = async {
                    Log.d("Chat", "Deleting user and lobby...")
                    val deleteUserAndLobbyResult = batchEndChat(
                        masterLobbyManager.getStoredLobbyCode().toString(),
                        current_uid.toString()
                    )
                    deleteUserAndLobbyResult.onSuccess {
                        Log.d("Chat", "User and lobby successfully deleted")
                    }.onFailure { error ->
                        Log.w(
                            "Chat",
                            "Error while deleting user and lobby with error code: ",
                            error
                        )
                    }
                    deleteUserAndLobbyResult
                }

                //await for the asks to finish
                deleteUserAndLobbyTask.await()

                Log.d("Chat", "All end chat tasks completed")


                // sign out of the user in auth directory
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


                Log.d("Chat", "Navigating back to home screen")
                NavigationManager.navigateToHomeScreen() //navigate back to home screen
            }
        } catch (error: Error) {
            Log.w("Chat", "An error occured while trying to end the chat: ${error}")
        }
    }
}