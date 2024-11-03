package com.example.a2chatAndroid.Utils

import android.util.Log
import com.example.a2chatAndroid.Navigation.NavigationManager
import com.example.a2chatAndroid.Network.CallBacks.masterLobbyManager
import com.example.a2chatAndroid.Network.Firebase.authGetCurrentUser
import com.example.a2chatAndroid.Network.Firebase.authSignInAnonymously
import com.example.a2chatAndroid.Network.RetrofitApi.firestoreAddUserToLobby
import com.example.a2chatAndroid.Network.RetrofitApi.firestoreCreateLobby
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope


//TODO: find a way to await for each step to finish before going
//starting a chat
suspend fun startChat() {
    Log.d("Chat", "Start chat called")
    try {
        //tasks need to finish before addding user to lobby
        coroutineScope {
            val deferredTasks = listOf(
                async(Dispatchers.IO) {
                    Log.d("Chat", "Creating Lobby...")
                    val apiCallToFirestore = firestoreCreateLobby()
                        .onSuccess { code ->
                            masterLobbyManager.onLobbyCreated(code)
                        }
                        .onFailure { error ->
                            Log.w("Chat", "Error while creating lobby with error code ", error)
                        }
               },
                async(Dispatchers.IO) {
                    Log.d("Chat", "Signing in Anonymously...")
                    authSignInAnonymously()
                }
            )
            deferredTasks.awaitAll()
        }



        Log.d("Chat", "User logged in after awaits: ${authGetCurrentUser()}")
        Log.d("Chat", "lobby code after awaits: ${masterLobbyManager.getStoredLobbyCode()}")

        //add user to lobby using the api call
        Log.d("Chat", "FireStoreAddUserToLobby called with uid: ${authGetCurrentUser()}, and lobby code ${masterLobbyManager.getStoredLobbyCode()}")
        firestoreAddUserToLobby(authGetCurrentUser().toString(),
            masterLobbyManager.getStoredLobbyCode().toString()
        )

        //TODO: add error handling if any of the above calls fail
        //navigate to chatScreen
        Log.d("Chat", "Navigation called")
        NavigationManager.navigateToChatScreen()

        //end of start chat functionality
        Log.d("Chat", "Start chat succesful")

    } catch(e: Error) {
        Log.w("Chat", "Start chat failed with exception: $e")
    }
}

//TODO: implement JoinChat
fun JoinChat(lobbyCode: String, uid: String) {
}

//TODO: implementEndChat
fun endChat() {
    //sign out user
    //delete all users from lobby using the api call
    //navigate back to home screen
}