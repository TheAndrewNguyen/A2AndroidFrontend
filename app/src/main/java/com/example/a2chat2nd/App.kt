package com.example.a2chat2nd

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Home") {
         composable("Home") { HomeScreen(navController) }
         composable("Join") { JoinScreen(navController) }
         composable("Create") { CreateAndChatScreen(navController) }
    }
}

