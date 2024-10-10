
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a2chat2nd.HomeScreen
import com.example.a2chat2nd.ui.theme.A2chat2ndTheme

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
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
    }
}



@Preview(showBackground = true)
@Composable
fun AppPreview() {
    A2chat2ndTheme {
        // To preview without navigation, you can remove navController for preview purposes
        val navController = rememberNavController()
        HomeScreen(navController)
    }
}
