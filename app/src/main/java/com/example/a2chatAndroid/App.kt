package com.example.a2chatAndroid

import androidx.compose.runtime.Composable
import com.example.a2chatAndroid.Managers.LifeCycleManager
import com.example.a2chatAndroid.Managers.NavHost

@Composable
fun App() {
    NavHost() //uis and navigation
    LifeCycleManager() //call the lifecycle manager
}