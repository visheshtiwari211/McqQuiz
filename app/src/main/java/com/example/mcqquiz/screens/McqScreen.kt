package com.example.mcqquiz.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mcqquiz.viewmodel.QuizViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun McqScreen(modifier: Modifier = Modifier, viewModel: QuizViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

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
                        option = option,
                        onClick = {
                            viewModel.markAsAnswered(current.mcqId)
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1.0f))

            Button(onClick = { viewModel.skipQuestion() }, modifier = Modifier.fillMaxWidth()) {
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
}

@Composable
fun OptionRow(modifier: Modifier = Modifier, option: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .graphicsLayer {
                shape = RoundedCornerShape(16.dp)
                clip = true
            }
            .background(color = Color.White)
            .clickable { onClick() },
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