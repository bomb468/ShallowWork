package com.example.tutorialrun2.screen

import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.tutorialrun2.MainNavigation
import com.example.tutorialrun2.R
import com.example.tutorialrun2.ui.theme.TutorialRun2Theme
import com.example.tutorialrun2.viewmodel.RequestPermissionPageViewModel

@Composable
fun RequestPermissionScreen(onPermissionGranted: () -> Unit){
    val requestPermissionPageViewModel = hiltViewModel<RequestPermissionPageViewModel>()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when(requestPermissionPageViewModel.hasDeniedPermissionValue){
            null -> {
                LoadingScreen()
            }
            true -> {
                ShowRationale()
            }
            false -> {
                AskPermission({
                    onPermissionGranted()
                }){
                    requestPermissionPageViewModel.setHasDeniedPermission()
                }
            }
        }
    }
}
@Composable
fun LoadingScreen() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.sandy_loading)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .aspectRatio(1f)
    )
}
@Composable
fun AskPermission(onPermissionGranted: () -> Unit, onPermissionDenied: () -> Unit){
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }
    LaunchedEffect(Unit) {
        // this will always be true to even reach this code
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
@Composable
fun ShowRationale(){
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = {
            // do nothing
        },
        title = { Text(text = "Notifications Required") },
        text = {
            Text("To provide the core features of this app, we need to send you notifications. Please enable them in the system settings to proceed.")
        },
        confirmButton = {
            Button(
                onClick = {
                    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    }
                    context.startActivity(intent)
                }
            ) {
                Text("Go to Settings")
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    LoadingScreen()
}