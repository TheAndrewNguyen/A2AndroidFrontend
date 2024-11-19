package com.example.a2chatAndroid.ui.Screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a2chatAndroid.ui.viewModels.JoinScreenViewModel

@Composable
fun JoinScreen() {
    val viewModel = JoinScreenViewModel()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        BackButton(viewModel)
        OTPInput(viewModel)
    }
}

@Composable
fun BackButton(viewModel: JoinScreenViewModel) {
    IconButton(onClick = {
        viewModel.navigateHome() // Navigate back to the home screen
    }, modifier = Modifier.offset(y = 16.dp)) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
            contentDescription = "Back to Home"
        )
    }
}

@Composable
fun OTPInput(viewModel: JoinScreenViewModel) {
    // State to hold the input for each of the 6 boxes
    val otpDigits = remember { mutableStateListOf("", "", "", "", "", "") }

    // Create a list of FocusRequesters for each text field
    val focusRequesters = remember { List(6) { FocusRequester() } }

    val keyBoardController = LocalSoftwareKeyboardController.current

    //reset OTP
    fun resetOTP() {
        otpDigits.indices.forEach { otpDigits[it] = "" }
        focusRequesters[0].requestFocus()
    }

    // Function to handle input change
    fun onInputChange(index: Int, newValue: String) {
        // Update the specific digit and move to the next box if the input is valid
        if (newValue.length <= 1) {
            otpDigits[index] = newValue // Update the specific index
            if (newValue.isNotEmpty() && index < 5) {
                // Focus next field if the current one is filled
                focusRequesters[index + 1].requestFocus()
            } else if (newValue.isEmpty() && index > 0) {
                // Focus previous field if the current one is empty and it's not the first field
                focusRequesters[index - 1].requestFocus()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center // Center the content vertically and horizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (i in 0 until 6) {
                    OTPBox(
                        digit = otpDigits[i],
                        onValueChange = { onInputChange(i, it) },
                        focusRequester = focusRequesters[i] // Pass the FocusRequester
                    )
                }
            }

            Button(
                onClick = {
                    val otpCode = otpDigits.joinToString("")
                    Log.d("JoinPage", "CurrentOTPCode on submit: ${otpCode}")

                    //join chat function
                    viewModel.joinChat(otpCode.toString(), otpCode.length)
                    keyBoardController?.hide()
                }
            )
            {
                Text("Submit")
            }
        }
    }
}

@Composable
fun OTPBox(digit: String, onValueChange: (String) -> Unit, focusRequester: FocusRequester) {
    TextField(
        value = digit,
        onValueChange = { newValue ->
            if (newValue.length <= 1) {
                onValueChange(newValue)
            }
        },
        modifier = Modifier
            .width(48.dp)
            .height(56.dp)
            .border(BorderStroke(1.dp, Color.Gray))
            .focusRequester(focusRequester), // Apply the FocusRequester
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next // Move to next field on Enter
        ),
        keyboardActions = KeyboardActions(
            onNext = { /* Optional: Move to next field when "Next" is pressed */ }
        ),
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = Color.Black,
            fontSize = 20.sp,
            lineHeight = 24.sp
        ),
        placeholder = {
            Text(
                text = "0",
                modifier = Modifier.padding(0.dp) // Padding for the placeholder
            )
        }
    )
}
