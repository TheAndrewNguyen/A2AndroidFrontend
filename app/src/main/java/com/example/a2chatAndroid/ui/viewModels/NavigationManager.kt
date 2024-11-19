package com.example.a2chatAndroid.ui.viewModels

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a2chatAndroid.ui.Screens.ChatScreen
import com.example.a2chatAndroid.ui.Screens.HomeScreen
import com.example.a2chatAndroid.ui.Screens.JoinScreen

@Composable
fun NavHost() {
    val navController = rememberNavController()
    NavigationManager.setNavController(navController)

    //navigation components
    NavHost(navController = navController, startDestination = "Home") {
        composable("Home") { HomeScreen() }
        composable("Join") { JoinScreen() }
        composable("Create") { ChatScreen() }
    }
}

//navigation functions
object NavigationManager {
    private lateinit var navController: NavController

    fun setNavController(controller: NavController) {
        navController = controller
    }

    fun navigateToHomeScreen() { //navigate to home screen
        navController.navigate("Home")
    }

    fun navigateToJoinScreen() { //navigate to join screen
        navController.navigate("Join")
    }

    fun navigateToChatScreen() { //navigate to create screen
        navController.navigate("Create")
    }
}