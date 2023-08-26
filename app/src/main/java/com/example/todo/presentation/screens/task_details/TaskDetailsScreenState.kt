package com.example.todo.presentation.screens.task_details

import com.example.todo.domain.entities.Task

sealed interface TaskDetailsScreenState {
    object Loading: TaskDetailsScreenState
    data class Success(
        val task: Task,
        val changesSaved: Boolean,
        val invalidName: Boolean
    ) : TaskDetailsScreenState
    object NotFound: TaskDetailsScreenState
}