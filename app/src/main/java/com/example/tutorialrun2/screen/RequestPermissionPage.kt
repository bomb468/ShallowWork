package com.example.tutorialrun2.screen

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
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

    // 1. Grab the primary color from your current theme
    val themePrimary = MaterialTheme.colorScheme.primary

    // 2. Create dynamic properties to "paint" the animation
    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = themePrimary.toArgb(), // Correct: Color to Int
            keyPath = arrayOf("**")
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.OPACITY,
            value = 100, // FIX: Remove the 'f'. Must be Int, not Float.
            keyPath = arrayOf("**")
        )
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            dynamicProperties = dynamicProperties, // The magic line
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .aspectRatio(1f)
        )
    }
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
        onDismissRequest = { /* Logic blocked by properties */ },
        // 1. Add an Icon for instant visual context
        icon = {
            Icon(
                imageVector = Icons.Default.NotificationsActive,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        // 2. Title should be centered if an icon is present (M3 Style)
        title = {
            Text(
                text = "Notifications Required",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        // 3. Supporting text should use the 'onSurfaceVariant' color
        text = {
            Text(
                text = "To provide the core features of this app, we need to send you notifications. Please enable them in the system settings to proceed.",
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            // 4. M3 recommends TextButton for dialogs to maintain a clean look
            TextButton(
                onClick = {
                    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    }
                    context.startActivity(intent)
                }
            ) {
                Text("Go to Settings", fontWeight = FontWeight.Bold)
            }
        },
        // M3 Dialogs have a specific shape (28dp) and tonal elevation
        shape = RoundedCornerShape(28.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    )
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun Preview() {
    TutorialRun2Theme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            LoadingScreen()
        }
    }
}