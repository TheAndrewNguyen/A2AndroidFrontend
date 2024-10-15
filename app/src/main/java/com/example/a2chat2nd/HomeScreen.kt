package com.example.a2chat2nd

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavHostController) {
    // State to track sign-in status
    val isUserSignedIn = remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance() // Get FirebaseAuth instance

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.titleLarge
        )

        // Button to sign in anonymously
        Button(
            onClick = {
                signOutAndSignInAnonymously(auth, isUserSignedIn) // Call the updated function
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.applegreen),
                contentColor = colorResource(id = R.color.white)
            ),
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Sign In Anonymously")
        }

        // Display sign-in status
        if (isUserSignedIn.value) {
            Text(text = "Sign in successful!", modifier = Modifier.padding(top = 16.dp))
        }

        // Join and Create buttons
        HomeScreenJoinButton(navController)
        HomeScreenCreateButton(navController)
    }
}

// Function to sign out and then sign in anonymously
private fun signOutAndSignInAnonymously(auth: FirebaseAuth, isUserSignedIn: MutableState<Boolean>) {
    // Sign out the current user
    auth.signOut() // Sign out the current user
    println("User signed out successfully")

    // Sign in anonymously
    auth.signInAnonymously()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign-in success
                isUserSignedIn.value = true // Update sign-in status
                println("Sign in successful: ${auth.currentUser?.uid}") // Log user ID
            } else {
                // If sign-in fails, log the error
                println("Error signing in: ${task.exception?.message}") // Log error message
            }
        }
}

@Composable
fun HomeScreenJoinButton(navController: NavController) {
    Button(
        onClick = { navController.navigate("Join") },
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.applegreen),
            contentColor = colorResource(id = R.color.white)
        ),
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = stringResource(id = R.string.join))
    }
}

@Composable
fun HomeScreenCreateButton(navController: NavController) {
    Button(
        onClick = { navController.navigate("Create") },
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.appleblue),
            contentColor = colorResource(id = R.color.white)
        ),
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = stringResource(id = R.string.create))
    }
}
