package com.example.a2chatAndroid.Navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.a2chatAndroid.Utils.endChat

//on the apps close
@Composable
fun LifeCycleManager() {
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if(event == Lifecycle.Event.ON_START) {
                Log.d("LifeCycle", "App has been opened")
            }
            else if (event == Lifecycle.Event.ON_STOP || event == Lifecycle.Event.ON_DESTROY) {
                endChat() //TODO: check if this is creating a bug
                Log.d("Lifecycle", "App has been closed out of")
            } else {
                Log.w("LifeCycle", "Unhandled Current event: $event")
            }
        }


        //adding observer to lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        //clean up the observer when we leave compositing
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
