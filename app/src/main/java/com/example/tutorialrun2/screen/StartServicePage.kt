package com.example.tutorialrun2.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun StartServicePage(onStartService : ()->Unit){
    Box(modifier= Modifier.fillMaxSize(),contentAlignment = Alignment.Center){
        Button(onClick = {
            onStartService()
        }){
            Text("Start Timer")
        }
    }
}
