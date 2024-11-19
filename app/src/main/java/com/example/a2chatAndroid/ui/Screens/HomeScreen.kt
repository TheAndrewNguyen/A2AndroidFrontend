package com.example.a2chatAndroid.ui.Screens

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a2chatAndroid.R
import com.example.a2chatAndroid.ui.viewModels.HomeScreenViewModel

@Composable
fun HomeScreen() {
    val viewModel = viewModel<HomeScreenViewModel>()

    // State to track sign-in status
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.titleLarge
        )

        // Join and Create buttons
        HomeScreenJoinButton(viewModel)
        HomeScreenCreateButton(viewModel)
    }
}


@Composable
fun HomeScreenJoinButton(viewModel: HomeScreenViewModel) {
    Button(
        onClick = { viewModel.navigateToJoinScreen() },
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
fun HomeScreenCreateButton(viewModel: HomeScreenViewModel) {
    Button(
        onClick = {
            viewModel.startChat()
        },
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
