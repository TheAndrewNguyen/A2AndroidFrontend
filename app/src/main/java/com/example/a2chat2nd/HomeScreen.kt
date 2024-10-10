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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController


@Composable
fun HomeScreen(navController: NavHostController) {
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
        HomeScreenJoinButton(navController)
        HomeScreenCreateButton(navController)
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
        modifier = Modifier
            .padding(8.dp)
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
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(text = stringResource(id = R.string.create))
    }
}