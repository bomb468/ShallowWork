package com.example.tutorialrun2.viewmodel

import android.os.Parcelable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty

class MainViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    @OptIn(SavedStateHandleSaveableApi::class)
    val backstack by savedStateHandle.saveable(
        saver = listSaver(
            save = { it.toList() },
            restore = { it.toMutableStateList() }
        )
    ) {
        mutableStateListOf<Screen>(Screen.StartServicePage)
    }
}
@Parcelize
sealed class Screen : Parcelable{
    data object StartServicePage : Screen()
    data object UIPage : Screen()
    data object RequestPermissionPage : Screen()
}

