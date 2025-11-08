package com.example.mcqquiz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcqquiz.models.McqUIModel
import com.example.mcqquiz.models.QuizUiState
import com.example.mcqquiz.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class QuizViewModel @Inject constructor(val repository: QuizRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllMcq()
        }
        viewModelScope.launch {
            repository.observeMcq().collect { list ->
                val nextIndex = list.indexOfFirst { !it.isAnswered }
                    .takeIf { it != -1 } ?: list.lastIndex

                val currentStreak = calculateCurrentStreak(list)
                val longestStreak = calculateLongestStreak(list)
                calculateScore(list)
                calculateSkippedQuestions(list)

                _uiState.update {
                    it.copy(
                        questions = list,
                        currentIndex = nextIndex,
                        currentStreak = currentStreak,
                        longestStreak = longestStreak,
                    )
                }
            }
        }
    }

    fun markAsAnswered(questionId: Int, isCorrectlyAnswered: Boolean) {
        viewModelScope.launch {
            repository.updateIsAnswered(mcqId = questionId, isAnswered = true, isCorrectlyAnswered, false)
        }
    }

    fun resetQuiz() {
        viewModelScope.launch {
            repository.resetQuiz()
            _uiState.update {
                QuizUiState()
            }
        }
    }

    fun skipQuestion(questionId: Int) {
        viewModelScope.launch {
           repository.updateIsAnswered(mcqId = questionId, isAnswered = true, isCorrect = false, isSkipped = true)
        }
        _uiState.update {
            val nextIndex = min(it.currentIndex + 1, it.questions.lastIndex)
            it.copy(currentIndex = nextIndex)
        }
    }

    private fun calculateCurrentStreak(questions: List<McqUIModel>): Int {
        var streak = 0
        val lastAnsweredIndex = questions.indexOfLast { it.isAnswered }
        if (lastAnsweredIndex == -1) return 0

        for (i in lastAnsweredIndex downTo 0) {
            val q = questions[i]
            if (!q.isCorrectlyAnswered) break
            streak++
        }

        return streak
    }

    private fun calculateLongestStreak(questions: List<McqUIModel>): Int {
        var longest = 0
        var current = 0

        for (q in questions) {
            if (q.isCorrectlyAnswered) {
                current++
                if (current > longest) longest = current
            } else {
                current = 0
            }
        }
        return longest
    }

    var totalScore = 0
    private fun calculateScore(questions: List<McqUIModel>) {
        totalScore = 0
        for (q in questions) {
            if (q.isCorrectlyAnswered) {
                totalScore++
            }
        }
    }

    var skippedCount = 0
    fun calculateSkippedQuestions(questions: List<McqUIModel>) {
        skippedCount = 0
        for (q in questions) {
            if (q.isSkipped) {
                skippedCount++
            }
        }
    }
}