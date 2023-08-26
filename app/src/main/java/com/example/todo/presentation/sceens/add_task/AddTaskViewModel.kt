package com.example.todo.presentation.sceens.add_task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.domain.TasksRepository
import com.example.todo.domain.entities.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val repository: TasksRepository
) : ViewModel() {

    private val _screenState: MutableStateFlow<AddTaskScreenState> = MutableStateFlow(
        AddTaskScreenState()
    )
    val screenState : StateFlow<AddTaskScreenState> = _screenState

    fun changeName(value: String) = _screenState.update { it.copy(name = value, invalidName = false) }

    fun changeDescription(value: String) = _screenState.update { it.copy(description = value) }

    fun changeIsCompleted(value: Boolean) = _screenState.update { it.copy(completed = value) }

    fun add() {
        viewModelScope.launch {
            if(_screenState.value.name.isBlank()) _screenState.update { it.copy(invalidName = true) }
            else {
                repository.addTask(
                    Task(
                        name = screenState.value.name,
                        description = screenState.value.description,
                        completed = screenState.value.completed
                    )
                )
                _screenState.update { it.copy(added = true) }
            }
        }
    }
}