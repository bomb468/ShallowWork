package com.example.tutorialrun2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tutorialrun2.service.ServiceInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartServicePageViewModel @Inject constructor(val serviceInfo: ServiceInfo) : ViewModel() {
    var selectedTotal by mutableIntStateOf(60)
    var selectedSession by mutableIntStateOf(10)
    fun updateServiceInfo(){
        serviceInfo.batchTimeDuration = selectedSession*60
        if (selectedTotal == -1) serviceInfo.totalSessions = Int.MAX_VALUE
        else serviceInfo.totalSessions = selectedTotal/selectedSession
    }
}