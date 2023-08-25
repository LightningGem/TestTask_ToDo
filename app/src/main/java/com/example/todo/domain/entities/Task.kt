package com.example.todo.domain.entities

data class Task(
    val id: Int,
    val name: String,
    val description: String,
    val completed: Boolean
)
