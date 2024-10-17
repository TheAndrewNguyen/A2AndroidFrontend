package com.example.a2chat2nd

import android.util.Log
import com.google.firebase.auth.FirebaseAuth


fun AuthSignOut(auth: FirebaseAuth) {
    auth.signOut()
    println("User signed out successfully")
}

fun AuthSignInAnonymously(auth: FirebaseAuth) {
    Log.d("Running", "this was run")
    auth.signInAnonymously()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign-in success
                Log.d("Auth", "Sign in succesful") //login succesful
            } else {
                // If sign-in fails, log the error
                Log.w("Auth", "sign in failture:", task.getException()) // Log error message
            }
        }
}

// Function to sign out and then sign in anonymously
fun signOutAndSignInAnonymously(auth: FirebaseAuth) {
    // Sign out the current user
    AuthSignOut(auth)

    // Sign in anonymously
    AuthSignInAnonymously(auth)
}

fun AuthDeleteAndSignOut(auth: FirebaseAuth) {
    val current_user = auth.currentUser

    Log.d("Auth", "$current_user")
    if(current_user == null) { //no user currently logged in
        Log.w("Auth", "NO CURRENT USER LOGGED IN")
    } else { //current user signed in
        current_user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Auth", "User account deleted.")
                    auth.signOut()
                } else {
                    Log.w("Auth", "User account deletion failed.")
                }
            }
    }

}
