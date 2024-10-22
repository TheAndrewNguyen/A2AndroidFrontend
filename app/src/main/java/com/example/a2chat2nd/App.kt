package com.example.a2chat2nd

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun App() {
    NavHost()
    LifeCycleManager() //call the lifecycle manager
}

@Composable
fun NavHost() {
    val navController = rememberNavController()
    NavigationManager.setNavController(navController)

    //navigation components
    NavHost(navController = navController, startDestination = "Home") {
        composable("Home") { HomeScreen() }
        composable("Join") { JoinScreen() }
        composable("Create") { CreateAndChatScreen() }
    }
}


//navigation functions
object NavigationManager {
    private lateinit var navController: NavController

    fun setNavController(conntroller: NavController) {
        navController = conntroller
    }

    fun NavigateToHomeScreen() { //navigate to home screen
        navController.navigate("Home")
    }


    fun NavigateToJoinScreen() { //navigate to join screen
        navController.navigate("Join")
    }

    fun NavigateToCreateScreen() { //navigate to create screen
        navController.navigate("Create")
    }

}