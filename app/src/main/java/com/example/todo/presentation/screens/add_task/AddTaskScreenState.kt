package com.example.todo.presentation.screens.add_task

data class AddTaskScreenState(
    val name: String = "",
    val description: String = "",
    val completed: Boolean = false,
    val invalidName: Boolean = false,
    val added: Boolean = false
)