package com.example.a2chatAndroid.UiScreens

//import com.example.a2chatAndroid.Utils.endChat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.a2chatAndroid.Network.CallBacks.masterLobbyManager
import com.example.a2chatAndroid.R


//Main composable
@Composable
fun ChatScreen() {
    val showPopup = remember { mutableStateOf(false) } // State to control popup visibility
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopStrip(showPopup)
        MessageDisplay()
        if (showPopup.value == true) {
            EndPopUp(showPopup)
        }
    }
}

//LAYOUT

//top strip
@Composable
fun TopStrip(showPopup: MutableState<Boolean>) {
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

//the red end button
@Composable
fun EndButton(onClick: () -> Unit) {
    Button(
        onClick = { onClick() }, //call the end popup
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.bootstrapRed),
        ),
    ) {
        Text(text = "End")
    }
}

//pop up when user presses end button
@Composable
fun EndPopUp(showPopUp: MutableState<Boolean>) {
    AlertDialog(
        onDismissRequest = { showPopUp.value = false },
        title = { Text(text = "Confirm leave") },
        text = { Text(text = "Are you sure you want to end the chat?") },
        confirmButton = {
            Button(
                onClick = {
                    //endChat()
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


//Message box
@Composable
fun MessageDisplay() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        Column (
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Message("Yolo3")
            Message("Yolo1")
            Message("ALEX GET THE DRUGS ")
            Message("ALEX WHERE ARE THE DRUGS!!!")
            Message("ALEX GIMMIE THE DRUGS!!!!!!1")
            Message("ALEX GIMMIE THE DRUGS!!!!!!1")
            Message("ALEX GIMMIE THE DRUGS!!!!!!1")
            Message("ALEX GIMMIE THE DRUGS!!!!!!1")
            Message("ALEX GIMMIE THE DRUGS!!!!!!1")
            Message("ALEX GIMMIE THE DRUGS!!!!!!1")
            Message("ALEX GIMMIE THE DRUGS!!!!!!1")
            Message("ALEX GIMMIE THE DRUGS!!!!!!1")
        }
        MessageInput()
    }
}

//Message input area
@Composable
fun MessageInput() {
    val message = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        TextField(
            value = message.value,
            onValueChange = { message.value = it },
            label = { Text("Message") },
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { focusState->
                    if(focusState.isFocused) {
                        keyboardController?.show()
                    }
                }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = { /*TODO SEND BUTTON */ },
            modifier = Modifier.weight(0.4f)
        ) {
            Text("Send")
        }
    }
}

//helper composables:

//Join code Composable part of top strip
@Composable
fun JoinCode () {
    var lobbyManager = masterLobbyManager
    Text("Lobby code " + lobbyManager.getStoredLobbyCode());
}

//messages
@Composable
fun Message(message: String) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .clip(shape = RoundedCornerShape(30.dp))
            .background(colorResource(R.color.appleblue))
            .padding(15.dp)
    ) {
        Column {
            Text(
                text = message,
                color = colorResource(R.color.white),
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}