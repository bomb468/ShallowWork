package com.example.tutorialrun2.service

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceInfo @Inject constructor(){
    var currentSession : Int = 1
    var totalSessions : Int = 4
    var currentTimer : Int = 0
    var batchTimeDuration : Int = 10*60
}