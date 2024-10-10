package com.example.a2chat2nd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a2chat2nd.ui.theme.A2chat2ndTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //val navController = rememberNavController()
            //App(navController);
            HomeScreen()
        }
    }
}
/*
fun App(navController: NavHostController) {
    NavHost(navController = navController, home = "Home")
}
*/

@Composable
fun HomeScreen() {
    Column (
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.titleLarge
        )
        HomeScreenJoinButton()
        HomeScreenCreateButton()
    }
}

@Composable
fun HomeScreenJoinButton() {
    Button(
        onClick = { /* Handle button click */ },
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.applegreen),
            contentColor = colorResource(id = R.color.white)
        ),
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(text = stringResource(id = R.string.join))
    }
}

@Composable
fun HomeScreenCreateButton() {
    Button(
        onClick = { /* Handle button click */ },
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.appleblue),
            contentColor = colorResource(id = R.color.white)
        ),
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .padding(8.dp)
        //comment test
    ) {
        Text(text = stringResource(id = R.string.create))
    }
}




@Preview(showBackground = true)
@Composable
fun AppPreview() {
    A2chat2ndTheme {
        HomeScreen()
    }
}