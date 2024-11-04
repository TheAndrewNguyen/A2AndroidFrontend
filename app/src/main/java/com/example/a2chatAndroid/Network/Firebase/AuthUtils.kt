package com.example.a2chatAndroid.Network.Firebase

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

//auth instance
private lateinit var auth: FirebaseAuth

//create connection to firebase and returns the instance
fun authCreateConnection(): FirebaseAuth {
    try {
        val auth = FirebaseAuth.getInstance()
        Log.d("Auth", "Connection to FirebaseAuth Successful")
        return auth
    } catch (e: Exception) {
        Log.w("Auth", "Connection to FirebaseAuth Failed exception $e") // Log the exception
        throw e // Rethrow the exception to handle it in the caller
    }
}

//sign out /end connection
fun authSignOut() {
    auth = Firebase.auth
    try {
        auth.signOut()
        Log.d("Auth", "User signed out successfully")
    } catch (e: Exception) {
        Log.w("Auth", "Sign out failed", e)
    }
}

//sign in anonymously and return user string
suspend fun authSignInAnonymously(): String? {
    auth = Firebase.auth

    //TODO: find out how to do signout outs asycly but for now this is fine

    //currently done asyncly
    return try {
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
        auth.currentUser?.uid //return the auth user's uid in string form
    } catch(e : Error) {
        Log.w("Auth", "There was a sign in error", e)
        null //return null
    }
}

fun authGetCurrentUser(): String? {
    return auth.currentUser?.uid
}

//removing user from auth and signing out the user
fun authDeleteAndSignOut() {
    val currentUser = Firebase.auth.currentUser

    if (currentUser == null) {
        Log.w("Auth", "No current user logged in")
        return
    }

    val userID = Firebase.auth.currentUser?.uid

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


