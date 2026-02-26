package com.example.tutorialrun2.viewmodel

import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import kotlinx.parcelize.Parcelize

class MainNavigationViewModel(savedStateHandle: SavedStateHandle, initialBoolean : Boolean) : ViewModel() {
    var hasPermission by mutableStateOf(initialBoolean)

    @OptIn(SavedStateHandleSaveableApi::class)
    val backstack by savedStateHandle.saveable(saver =
        listSaver(save = { it.toList() }, restore = { it.toMutableStateList() })) {
        mutableStateListOf<Screen>(Screen.StartServicePage)
    }
}
@Parcelize
sealed class Screen : Parcelable{
    data object StartServicePage : Screen()
    data object UIPage : Screen()
}

