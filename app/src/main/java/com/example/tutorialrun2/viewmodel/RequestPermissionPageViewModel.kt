package com.example.tutorialrun2.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

val Context.dataStore by preferencesDataStore(name = "user_settings")

@HiltViewModel
class RequestPermissionPageViewModel @Inject constructor(@param:ApplicationContext val context : Context) : ViewModel() {
    private val key = booleanPreferencesKey("hasDeniedPostNotificationPermission")
    var hasDeniedPermissionValue : Boolean? by mutableStateOf(null)

    init{
        /*
        viewModelScope.launch {
            delay(5000)
            hasDeniedPermissionValue=false
        }*/
        viewModelScope.launch{
            context.dataStore.data
                .map{
                    preferences ->
                        delay(5000)
                        preferences[key] ?: false
                }
                .catch {
                    emit(false)
                }
                .collect {
                    hasDeniedPermissionValue = it
                }
        }
    }
    fun setHasDeniedPermission(){
        hasDeniedPermissionValue=null
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences[key] = true
            }
        }
    }
}