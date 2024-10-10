package com.example.a2chat2nd
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController() // Make sure to import this
            App(navController)
        }
    }
}

@Composable
fun App(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "test") {
        composable("home") { HomeScreen(navController) }
        composable("test") {test(navController)}
    }
}

@Composable
fun test(navController: NavHostController) {
    Text("Please work")
    Button(
        onClick = { navController.navigate("home") }
    ) {
        Text(text="please work")
    }
}



