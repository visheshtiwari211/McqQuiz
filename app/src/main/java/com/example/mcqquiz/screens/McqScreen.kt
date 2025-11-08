package com.example.mcqquiz.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mcqquiz.viewmodel.QuizViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun McqScreen(
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel,
    onNavigateToResultsScreen: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showStreakAnimation by remember { mutableStateOf(false) }
    var previousStreak by remember { mutableIntStateOf(0) }


    LaunchedEffect(state.currentStreak) {
        val current = state.currentStreak
        if (current > 0 &&
            current % 3 == 0 &&
            previousStreak != current
        ) {
            showStreakAnimation = true
            delay(3000)
            showStreakAnimation = false
        }

        previousStreak = current
    }

    LaunchedEffect(state.questions) {
        if (state.questions.isNotEmpty() && state.questions.all { it.isAnswered }) {
            onNavigateToResultsScreen()
        }
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (state.questions.isNotEmpty()) {
            val current = state.questions[state.currentIndex]
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        LinearProgressIndicator(
                            progress = {
                                state.currentIndex.toFloat() / state.questions.size
                            },
                            color = Color.Black,
                            gapSize = 4.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(text = "${state.currentIndex}/${state.questions.size}")
                        }
                        Text(current.question)
                    }

                    current.options.forEachIndexed { index, option ->
                        OptionRow(
                            index = index,
                            correctIndex = current.correctOptionIndex,
                            option = option,
                            mcqId = current.mcqId,
                            onClick = {
                                coroutineScope.launch {
                                    delay(2000)
                                    viewModel.markAsAnswered(
                                        current.mcqId,
                                        isCorrectlyAnswered = current.correctOptionIndex == index
                                    )
                                }
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1.0f))

                Button(
                    onClick = { viewModel.skipQuestion(current.mcqId) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Skip")
                }
            }
        } else {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "No Questions")
            }
        }

        AnimatedVisibility(
            visible = showStreakAnimation,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 700, easing = LinearOutSlowInEasing)
            ) + fadeIn(),
            exit = fadeOut(tween(400))
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF00C853),
                                    Color(0xFF64DD17)
                                )
                            )
                        )
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Congratulations!",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "You're on a streak of ${state.currentStreak}!",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OptionRow(
    modifier: Modifier = Modifier,
    option: String, onClick: () -> Unit,
    index: Int,
    correctIndex: Int,
    mcqId: Int
) {
    var isClicked by remember(mcqId) { mutableStateOf(false) }
    val bgColor by remember(isClicked, correctIndex, index) {
        derivedStateOf {
            when {
                isClicked && correctIndex == index -> Color.Green
                isClicked && correctIndex != index -> Color.Red
                else -> Color.White
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .graphicsLayer {
                shape = RoundedCornerShape(16.dp)
                clip = true
            }
            .background(color = bgColor)
            .clickable {
                isClicked = true
                onClick()
            },
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = option)
        }
    }
}