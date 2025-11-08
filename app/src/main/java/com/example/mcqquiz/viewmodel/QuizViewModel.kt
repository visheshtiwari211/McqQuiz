package com.example.mcqquiz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            repository.getAllMcq().collect { list ->
                val nextIndex = list.indexOfFirst { !it.isAnswered }.takeIf { it != -1 } ?: list.lastIndex
                _uiState.update {
                    it.copy(
                        questions = list,
                        currentIndex = nextIndex
                    )
                }
            }
        }
    }

    fun markAsAnswered(questionId: Int) {
        viewModelScope.launch {
            repository.updateIsAnswered(mcqId = questionId, isAnswered = true)
        }
    }

    fun skipQuestion() {
        _uiState.update {
            val nextIndex = min(it.currentIndex + 1, it.questions.lastIndex)
            it.copy(currentIndex = nextIndex)
        }
    }
}