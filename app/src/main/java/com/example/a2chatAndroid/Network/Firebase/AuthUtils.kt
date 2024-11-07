package com.example.a2chatAndroid.Network.Firebase

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

//auth instance
private lateinit var auth: FirebaseAuth

//get current user
fun authGetCurrentUser(): String? {
    return auth.currentUser?.uid
}

//sign out /end connection
suspend fun authSignOut(): Result<String> = suspendCancellableCoroutine { cont->
    auth = Firebase.auth
    if(auth.currentUser != null) {
        try {
            auth.signOut()
            Log.d("Auth", "User signed out successfully")
            cont.resume(Result.success("Auth Signed out successfully"))

        } catch (e: Exception) {
            Log.w("Auth", "Sign out failed", e)
            cont.resumeWithException(e)
        }

    } else {
        Log.d("Auth", "User is already signed out")
        cont.resume(Result.success("User is already signed out"))
    }
}

//sign in anonymously and return user string
suspend fun authSignInAnonymously(): Result<String> = suspendCancellableCoroutine { cont->
    auth = Firebase.auth

    try {
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign-in success
                    Log.d("Auth", "Sign in succesful") //login succesful
                    cont.resume(Result.success("Sign in succesful"))
                } else {
                    // If sign-in fails, log the error
                    Log.w("Auth", "sign in failure:", task.getException()) // Log error message
                    cont.resumeWithException(task.getException()!!)
                }
            }
    } catch(e : Exception) {
        Log.w("Auth", "There was a sign in error", e)
        cont.resumeWithException(e)
    }
}

//signout and then sign in just in case the user was previously not signed out
//signs in fresh user
suspend fun safeSignOutandSignInAnonymously() : Result<String>{
    return try {

        Log.d("Auth", "Sign out called")
        authSignOut()

        //sign in
        authSignInAnonymously()
        Log.d("Auth", "Sign in called")

        Result.success("Sign out and sign in successful with UID: ${authGetCurrentUser()}")
    } catch(e: Error) {
        Log.w("Auth", "Sign out and sign in failed", e)
        Result.failure(Exception("Sign out and sign in failed: ${e}"))
    }
}

