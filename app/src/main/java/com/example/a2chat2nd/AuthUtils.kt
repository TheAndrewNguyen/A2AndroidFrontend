package com.example.a2chat2nd

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

//create connection to firebase
fun authCreateConnection(): FirebaseAuth {
    return try {
        val auth = FirebaseAuth.getInstance()
        Log.d("Auth", "Connection to FirebaseAuth Succesful")
        auth
    } catch(e : Exception) {
        Log.w("Auth", "Connection to FirebaseAuth Failed exception $e") // Log the exception
        throw e // Rethrow the exception to handle it in the caller
    }
}

//get current user that is signed in
fun authGetCurrentUser(): FirebaseUser? {
    val auth = authCreateConnection()

    val current_user = auth.currentUser

    if(current_user == null) {
        Log.w("Auth", "No current user logged in")
    } else {
        Log.d("Auth", "Current user: $current_user")
    }

    return auth.currentUser //might be null if no current user see log cat
}

//sign out /end connection
fun authSignOut() {
    val auth = authCreateConnection()
    try {
        auth.signOut()
        Log.d("Auth", "User signed out successfully")
    } catch (e: Exception) {
        Log.w("Auth", "Sign out failed", e)
    }
}

//sign in anonymously
fun authSignInAnonymously() {
    val auth = authCreateConnection()
    auth.signInAnonymously()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign-in success
                Log.d("Auth", "Sign in succesful") //login succesful
            } else {
                // If sign-in fails, log the error
                Log.w("Auth", "sign in failure:", task.getException()) // Log error message
            }
        }
}

// Function to sign out and then sign in anonymously
fun authSignOutAndSignInAnonymously() {
    authSignOut()           // Sign out the current user
    authSignInAnonymously() // Sign in anonymously
}

//removing user from auth and signing out the user
// TODO: BUG IN LOGGING CHECK REAL AUTH FOR TESTING will fix later with threads
fun authDeleteAndSignOut() {
    val auth = authCreateConnection()       //connect to auth

    val current_user = authGetCurrentUser()

    if(current_user == null) {
        Log.w("Auth", "No current user logged in")
        return
    }

    val userID = current_user.uid

    current_user.delete()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val updatedUser = auth.currentUser
                if (updatedUser == null) {
                    Log.d("Auth", "$userID deleted from database")
                    auth.signOut()
                    Log.d("Auth", "$userID signed out")
                }
            } else {
                Log.w("Auth", "$userID deletion failed.")
            }
        }
}


