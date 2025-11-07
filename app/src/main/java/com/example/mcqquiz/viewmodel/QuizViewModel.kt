package com.example.mcqquiz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcqquiz.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(val repository: QuizRepository) : ViewModel() {
    val getAllMcqFlow = repository.getAllMcqFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = listOf()
    )

    init {
        viewModelScope.launch {
            repository.getAllMcq()
        }
    }

    fun updateIsAnswered(mcqId: Int, isAnswered: Boolean) {
        viewModelScope.launch {
            repository.updateIsAnswered(mcqId = mcqId, isAnswered = isAnswered)
        }
    }
}