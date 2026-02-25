package com.example.tutorialrun2

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.tutorialrun2.screen.RequestPermissionScreen
import com.example.tutorialrun2.screen.StartServicePage
import com.example.tutorialrun2.screen.UiPage
import com.example.tutorialrun2.ui.theme.TutorialRun2Theme
import com.example.tutorialrun2.viewmodel.MainActivityViewModel
import com.example.tutorialrun2.viewmodel.MainNavigationViewModel
import com.example.tutorialrun2.viewmodel.Screen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel : MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TutorialRun2Theme(darkTheme = isSystemInDarkTheme(), dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation()
                }
            }
        }
    }
}
fun provideMainViewModel() : ViewModelProvider.Factory {
    return viewModelFactory {
        initializer {
            val savedStateHandle = createSavedStateHandle()
            MainNavigationViewModel(savedStateHandle)
        }
    }
}
@Composable
fun MainNavigation(){
    val mainViewModel = viewModel<MainNavigationViewModel>(factory = provideMainViewModel())
    NavDisplay(
        entryDecorators = listOf(
            // Add the default decorators for managing scenes and saving state
            rememberSaveableStateHolderNavEntryDecorator(),
            // Then add the view model store decorator
            rememberViewModelStoreNavEntryDecorator()
        ),
        backStack = mainViewModel.backstack,
        onBack = { mainViewModel.backstack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is Screen.StartServicePage -> NavEntry(key) {
                    StartServicePage { mainViewModel.backstack.add(Screen.UIPage) }
                }
                is Screen.UIPage -> NavEntry(key) {
                    UiPage()
                }
                is Screen.RequestPermissionPage -> NavEntry(key){
                    RequestPermissionScreen {
                        mainViewModel.backstack.removeLastOrNull()
                        mainViewModel.backstack.add(Screen.StartServicePage)
                    }
                }
            }
        }
    )
}

@Composable
fun CircleUITestFunction(){
    val circleViewModel = viewModel<CircleViewModel>()
    Column(
        modifier = Modifier.systemBarsPadding().fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        CircularTimer(circleViewModel.currentFloatSmallArc,
            circleViewModel.currentFloatBigArc,
            circleViewModel.secondsDone.toInt()
        )
        Button(onClick = {
            circleViewModel.startFlow()
            circleViewModel.buttonClicked = true
        }, enabled = !circleViewModel.buttonClicked) {
            Text("Start Timer")
        }
    }
}
@Composable
fun CircularTimer(inputOfSmallArc: Float,inputOfBigArc: Float,secondsDone : Int) {
    val animatedSweepOfSmallArc by animateFloatAsState(
        targetValue = inputOfSmallArc,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
    )
    val animatedSweepOfBigArc by animateFloatAsState(
        targetValue = inputOfBigArc,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
    )
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(250.dp).padding(20.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = Color.LightGray,
                startAngle = 120f,
                sweepAngle = 300f,
                useCenter = false,
                style = Stroke(width = 15.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = Color.Magenta,
                startAngle = 120f,
                sweepAngle = animatedSweepOfBigArc,
                useCenter = false,
                style = Stroke(width = 15.dp.toPx(), cap = StrokeCap.Round)
            )
            inset(25.dp.toPx()) {
                drawArc(
                    color = Color.LightGray,
                    startAngle = 120f,
                    sweepAngle = 300f,
                    useCenter = false,
                    style = Stroke(width = 15.dp.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    color = Color.Blue,
                    startAngle = 120f,
                    sweepAngle = animatedSweepOfSmallArc,
                    useCenter = false,
                    style = Stroke(width = 15.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }
        Text("${
            if (secondsDone/60>=10) secondsDone/60
            else "0${secondsDone/60}"
        }:${
            if (secondsDone%60>=10) secondsDone%60
            else "0${secondsDone%60}"
        }")
    }
}

class CircleViewModel : ViewModel(){
    var currentFloatBigArc by mutableFloatStateOf(0f)
    var finalFloatBigArc by mutableFloatStateOf(300f)

    var currentFloatSmallArc by mutableFloatStateOf(0f)

    var buttonClicked by mutableStateOf(false)

    var secondsDone by mutableFloatStateOf(0f)
    val totalSecondsInBatch = 30f
    val totalSeconds = 60f
    fun startFlow() {
        viewModelScope.launch {
            try{
                flow {
                    delay(1000)
                    while (secondsDone<totalSecondsInBatch) {
                        secondsDone += 1
                        emit(secondsDone)
                        delay(1000)
                    }
                }.collect{
                    currentFloatSmallArc = ((it%totalSeconds)/totalSeconds)*finalFloatBigArc
                    currentFloatBigArc = ((it%totalSecondsInBatch)/totalSecondsInBatch)*finalFloatBigArc
                }
            }finally {
                buttonClicked=false
                secondsDone=0f
                currentFloatBigArc=0f
                currentFloatSmallArc=0f
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    TutorialRun2Theme {
        MainNavigation()
    }
}