package com.example.todo.presentation.screens.task_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.domain.TasksRepository
import com.example.todo.domain.entities.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    private val repository: TasksRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val ID_KEY = "task_id"
    }

    val id: Int? = savedStateHandle.get<Int>(ID_KEY)

    private val _screenState: MutableStateFlow<TaskDetailsScreenState> = MutableStateFlow(
        if(id == null) TaskDetailsScreenState.NotFound else TaskDetailsScreenState.Loading
    )

    val screenState : StateFlow<TaskDetailsScreenState> = _screenState

    private val editedTask = MutableStateFlow<Task?>(null)

    init {
        if(id != null) combine(
            editedTask,
            repository.tasksFlow().map { it.firstOrNull { it.id == id } }
        ) { edited, actual ->
            _screenState.update {
                if(actual == null) TaskDetailsScreenState.NotFound
                else TaskDetailsScreenState.Success(
                    task = edited ?: actual,
                    changesSaved = edited == null || edited == actual,
                    invalidName = it is TaskDetailsScreenState.Success && it.invalidName
                )
            }
        }.launchIn(viewModelScope)
    }

    fun deleteTask() {
        val currentState = _screenState.value
        if(currentState is TaskDetailsScreenState.Success) {
            viewModelScope.coroutineContext.cancelChildren()
            viewModelScope.launch(NonCancellable) {
                repository.deleteTasks(setOf(currentState.task.id))
            }
        }
    }

    fun changeName(value: String) {
        val currentState = _screenState.value
        if(currentState is TaskDetailsScreenState.Success) {
            _screenState.value = currentState.copy(invalidName = false)
            editedTask.update {
                it?.copy(name = value) ?: currentState.task.copy(name = value)
            }
        }
    }

    fun changeDescription(value: String) {
        val currentState = _screenState.value
        if(currentState is TaskDetailsScreenState.Success) editedTask.update {
            it?.copy(description = value) ?: currentState.task.copy(description = value)
        }
    }

    fun changeIsCompleted(value: Boolean) {
        val currentState = _screenState.value
        if(currentState is TaskDetailsScreenState.Success) editedTask.update {
            it?.copy(completed = value) ?: currentState.task.copy(completed = value)
        }
    }

    fun saveChanges() {
        val currentState = _screenState.value
        if(currentState is TaskDetailsScreenState.Success && !currentState.changesSaved) {
            if(currentState.task.name.isBlank()) _screenState.value = currentState.copy(invalidName = true)
            else viewModelScope.launch { repository.updateTask(currentState.task) }
        }
    }
}