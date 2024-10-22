package com.example.a2chatAndroid.Firebase

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

// TODO: Need to find a way to store this on creation better
var documentID = ""; //document id of the lobby

@Composable
fun NavigateToJoinScreen() { //navigate to join screen
    val navController = rememberNavController()
    navController.navigate("Join")
}

@Composable
fun NavigateToCreateScreen() { //navigate to create screen
    val navController = rememberNavController()
    navController.navigate("Create")
}

//connect to firestore instance
fun connectToFireStore(): FirebaseFirestore {
    try {
        val db = Firebase.firestore
        Log.d("FireStore", "db connected")
        return db; // Return the Firestore instance
    } catch (e: Exception) {
        Log.w("FireStore", "db connection failed", e)
        throw e // Rethrow the exception to handle it in the caller
    }
}

fun test_schema(): Map<String, Any> {
    Log.w("FireStore", "Test schema ran")
    val current_user = authGetCurrentUser()
    val test = hashMapOf(
        "isActive" to true,
        "lobbyId" to "0000",
        "users" to (current_user?.uid ?: "No user logged in")
    )

    return test
}

//adding a lobby/document document
fun createLobby() {
    val db = connectToFireStore()
    val test = test_schema()
    Log.w("FireStore", "Prepping lobby with data $test")

    db.collection("lobbies")
        .add(test)
        .addOnSuccessListener { documentReference ->
            val lobbyId = documentReference.id
            documentID = lobbyId
            Log.d("FireStore", "DocumentSnapshot added with ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            Log.w("FireStore", "Error adding document", e)
        }
}

//deleting a lobby / document
fun deleteLobby() {
    Log.d("Firestore", "Deletion started")
    val db = connectToFireStore()
    val collection = db.collection("lobbies")

    collection.document(documentID)
        .delete()
        .addOnSuccessListener {Log.d("FireStore", "Document $documentID succesfully deleted")}
        .addOnFailureListener{Log.w("FireStore", "Document failed to delete")}
}

