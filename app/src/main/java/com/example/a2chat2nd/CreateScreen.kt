package com.example.a2chat2nd

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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

/*TODO: features:
 - navigation: end button --> confirm leave
 - design:
  - top: join code end button
  - middle: chat screen
  - bottom: message box, send button
*/

@Composable
fun CreateScreen(navController: NavController) {
    val showPopup = remember { mutableStateOf(false) } // State to control popup visibility

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopStrip(navController, showPopup)
        if (showPopup.value) {
            EndPopUp(navController, showPopup)
        }
    }
}




//top strip components
@Composable
fun TopStrip(navController: NavController, showPopup: MutableState<Boolean>) {
    Row(
        modifier = Modifier
            .padding(top = 16.dp)
            .height(75.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        JoinCode()
        EndButton { showPopup.value = true }
    }
}

@Composable
fun JoinCode () {
    Text(stringResource(id = R.string.JoinCode));
}

@Composable
fun EndButton(onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.bootstrapRed),
        ),
    ) {
        Text(text = "End")
    }
}

//pop up
@Composable
fun EndPopUp(navController: NavController, showPopUp: MutableState<Boolean>) {
    AlertDialog(
        onDismissRequest = { showPopUp.value = false },
        title = { Text(text = "Confirm leave") },
        text = { Text(text = "Are you sure you want to end the chat?") },
        confirmButton = {
            Button(
                onClick = {
                    navController.navigate("Home") // Navigate on confirmation
                    showPopUp.value = false // Close the dialog
                }
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button(
                onClick = { showPopUp.value = false } // Just close the dialog
            ) {
                Text("No")
            }
        }
    )
}





