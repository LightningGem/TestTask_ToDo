package com.example.todo.domain

import com.example.todo.domain.entities.Task
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    fun tasksFlow() : Flow<List<Task>>
    suspend fun addTask(value: Task)
    suspend fun updateTask(value: Task)
    suspend fun deleteTasks(taskIds: Set<Int>)
}