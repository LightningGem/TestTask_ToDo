package com.example.todo.presentation.screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.domain.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(private val repository: TasksRepository) : ViewModel() {

    private val _screenState: MutableStateFlow<TasksScreenState> = MutableStateFlow(TasksScreenState.Loading)
    val screenState : StateFlow<TasksScreenState> = _screenState

    init {
        viewModelScope.launch {
            repository.tasksFlow().collect { tasks ->
                _screenState.update { currentState ->
                    when(currentState) {
                        is TasksScreenState.Loading -> TasksScreenState.Loaded(tasks = tasks, selectedTaskIds = emptySet())
                        is TasksScreenState.Loaded -> currentState.copy(
                            tasks = tasks,
                            selectedTaskIds = buildSet {
                                tasks.forEach {
                                    if(currentState.selectedTaskIds.contains(it.id)) add(it.id)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    fun deleteTasks(taskIds: Set<Int>) {
        viewModelScope.launch {
            repository.deleteTasks(taskIds)
        }
    }

    fun changeIsTaskCompleted(taskId: Int, value: Boolean) {
        val currentState = screenState.value
        if(currentState is TasksScreenState.Loaded) viewModelScope.launch {
            currentState.tasks.firstOrNull { it.id == taskId }?.let {
                repository.updateTask(
                    it.copy(completed = value)
                )
            }
        }
    }

    fun changeSelectedTasks(taskIds: Set<Int>) {
        _screenState.update { currentState ->
            when(currentState) {
                is TasksScreenState.Loading -> currentState
                is TasksScreenState.Loaded -> currentState.copy(selectedTaskIds = taskIds)
            }
        }
    }
}