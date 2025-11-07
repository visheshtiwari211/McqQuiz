package com.example.mcqquiz.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mcqquiz.viewmodel.QuizViewModel

@Composable
fun McqScreen(modifier: Modifier = Modifier, viewModel: QuizViewModel) {
    val quizList = viewModel.getAllMcqFlow.collectAsStateWithLifecycle()
    if (quizList.value.isNotEmpty()) {
        val answeredQuestion by remember {
            derivedStateOf { quizList.value.filter { it.isAnswered }.size }
        }
        val totalQuestions by remember {
            mutableIntStateOf(quizList.value.size)
        }
        val progress by remember { mutableIntStateOf(answeredQuestion / totalQuestions) }
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LinearProgressIndicator(
                    progress = { progress.toFloat() },
                    color = Color.Blue,
                    gapSize = 4.dp,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentHeight()
                )
                Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                    Spacer(modifier = Modifier.weight(1.0f))
                    Text(text = "$answeredQuestion/$totalQuestions")
                }
            }

        }
    }
}