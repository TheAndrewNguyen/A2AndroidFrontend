package com.example.a2chatAndroid.Network.Firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

val auth = authCreateConnection()

//create connection to firebase and returns the instance
fun authCreateConnection(): FirebaseAuth {
    return try {
        val auth = FirebaseAuth.getInstance()
        Log.d("Auth", "Connection to FirebaseAuth Successful")
        return auth
    } catch(e : Exception) {
        Log.w("Auth", "Connection to FirebaseAuth Failed exception $e") // Log the exception
        throw e // Rethrow the exception to handle it in the caller
    }
}

//TODO: Fix to instance thing
//get current user that is signed in
fun authGetCurrentUser(): FirebaseUser? {
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
    try {
        auth.signOut()
        Log.d("Auth", "User signed out successfully")
    } catch (e: Exception) {
        Log.w("Auth", "Sign out failed", e)
    }
}

//sign in anonymously
fun authSignInAnonymously() {
    authSignOut() //sign out first just in case
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

//removing user from auth and signing out the user
fun authDeleteAndSignOut() {
    val currentUser = authGetCurrentUser()

    if(currentUser == null) {
        Log.w("Auth", "No current user logged in")
        return
    }

    val userID = currentUser.uid

    currentUser.delete()
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


