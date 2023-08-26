package com.example.todo.presentation.screens.tasks

import com.example.todo.domain.entities.Task

sealed interface TasksScreenState {
    object Loading: TasksScreenState
    data class Loaded(val tasks: List<Task>, val selectedTaskIds: Set<Int>) : TasksScreenState
}