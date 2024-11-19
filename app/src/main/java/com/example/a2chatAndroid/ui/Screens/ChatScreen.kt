package com.example.a2chatAndroid.ui.Screens

//import com.example.a2chatAndroid.Utils.endChat
import android.util.Log
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.a2chatAndroid.Managers.endChat
import com.example.a2chatAndroid.Network.Api.Service.sendMessage
import com.example.a2chatAndroid.Network.CallBacks.masterLobbyManager
import com.example.a2chatAndroid.Network.Firebase.authGetCurrentUser
import com.example.a2chatAndroid.R
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.launch


//Main composable
@Composable
fun ChatScreen() {
    val currentListOfMessages = remember { mutableStateListOf<messageData>() }
    val showPopup = remember { mutableStateOf(false) } // State to control popup visibility


    val currentUser = authGetCurrentUser()

    //imports for database
    val realTimeDataBase = Firebase.database
    val lobbyId = masterLobbyManager.getStoredLobbyCode()

    //todo: implement realtime data updates
    LaunchedEffect(Unit) {
        val messageListener = object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val snapshotMessageList = mutableStateListOf<messageData>()

                currentListOfMessages.clear()
                snapshot.children.forEach {
                    val uid = it.child("userId").value.toString()
                    val fromUser = uid == currentUser
                    val message = it.child("messageContent").value.toString()

                    val toDataModel = messageData(message, fromUser)
                    snapshotMessageList.add(toDataModel)
                }

                //update the global list of messages
                Log.d("ChatRoom", "Messages: $snapshotMessageList")
                currentListOfMessages.clear()
                currentListOfMessages.addAll(snapshotMessageList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("ChatRoom", "Failed to read value")
            }
        }

        val lobbyRef = realTimeDataBase.getReference("messages/$lobbyId")
        lobbyRef.orderByChild("timestamp").addValueEventListener(messageListener) //sort in firebase when called
    }


    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopStrip(showPopup)
        MessageDisplay(currentListOfMessages)
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
    val coroutineScope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = { showPopUp.value = false },
        title = { Text(text = "Confirm leave") },
        text = { Text(text = "Are you sure you want to end the chat?") },
        confirmButton = {

            Button(
                onClick = {
                    try {
                    coroutineScope.launch {
                        endChat()
                    }} catch (e: Error) {
                        Log.w("EndPopUp", "An error occured while trying to end the chat: ${e}")
                    }
                }
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button(
                onClick = { showPopUp.value = false } // Just close the dialog and continue the chat
            ) {
                Text("No")
            }
        }
    )
}


//Message box
@Composable
fun MessageDisplay(messageList : MutableList<messageData>) {
    val scrollState = rememberScrollState()

    LaunchedEffect(messageList.size) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }
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
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            for(messageData in messageList) {
             Message(messageData)
            }
        }
        MessageInput()
    }
}

//Message input area
@Composable
fun MessageInput() {
    val coroutineScope = rememberCoroutineScope()
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
            onClick = {
                coroutineScope.launch() {
                    if(message.value.isEmpty()) return@launch
                    sendMessage(message.value)
                    message.value = ""
                    keyboardController?.hide()
                }
            },
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

data class messageData(
    val message: String,
    val fromUser: Boolean
)

//messages
@Composable
fun Message(messageData: messageData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (messageData.fromUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .clip(shape = RoundedCornerShape(30.dp))
                .background(
                    if(messageData.fromUser) colorResource(R.color.appleblue)
                    else {
                        colorResource(R.color.applegreen)
                    }
                )
                .padding(15.dp)
        ) {
            Column {
                Text( //user idenfier
                    text = if(messageData.fromUser) "You" else "User",
                    color = colorResource(R.color.black),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text( //user text message
                    text = messageData.message,
                    color = colorResource(R.color.white),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}