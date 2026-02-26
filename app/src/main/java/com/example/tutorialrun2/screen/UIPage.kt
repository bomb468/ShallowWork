package com.example.tutorialrun2.screen

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tutorialrun2.ui.theme.TutorialRun2Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@Composable
fun UiPage(){
    CircleUITestFunction()
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


@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun Preview() {
    TutorialRun2Theme() {
        UiPage()
    }
}