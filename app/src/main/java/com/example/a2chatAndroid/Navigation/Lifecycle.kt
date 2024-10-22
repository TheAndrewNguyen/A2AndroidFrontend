package com.example.a2chatAndroid.Navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.a2chatAndroid.Firebase.authDeleteAndSignOut

//on the apps close
@Composable
fun LifeCycleManager() {
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if(event == Lifecycle.Event.ON_START) {
                onAppOpen()
                Log.d("LifeCycle", "App has been opened")
            }
            else if (event == Lifecycle.Event.ON_STOP || event == Lifecycle.Event.ON_DESTROY) {
                onAppClose()
                Log.d("Lifecycle", "App has been closed out of")
            } else {
                Log.w("LifeCycle", "Nonhandled Current event: $event")
            }
        }


        //adding observer to lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        //clean up the oberserver when we leave compositino
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

fun onAppOpen() {
    NavigationManager.NavigateToHomeScreen() //navigate the user back home screen
}

fun onAppClose() {
    authDeleteAndSignOut()
}