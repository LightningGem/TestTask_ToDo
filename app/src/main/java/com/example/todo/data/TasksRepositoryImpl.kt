package com.example.todo.data

import com.example.todo.data.room.TaskDao
import com.example.todo.data.room.TaskEntity
import com.example.todo.domain.TasksRepository
import com.example.todo.domain.entities.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TasksRepositoryImpl @Inject constructor(private val dao: TaskDao) : TasksRepository {

    override fun tasksFlow(): Flow<List<Task>> = dao.getAll().map { list ->
        list.map { entity ->
            Task(
                id = entity.id,
                name = entity.name,
                description = entity.description,
                completed = entity.completed
            )
        }
    }

    override suspend fun addTask(value: Task) = dao.insert(
        TaskEntity(
            id = value.id,
            name = value.name,
            description = value.description,
            completed = value.completed
        )
    )

    override suspend fun updateTask(value: Task) = dao.update(
        TaskEntity(
            id = value.id,
            name = value.name,
            description = value.description,
            completed = value.completed
        )
    )

    override suspend fun deleteTasks(taskIds: Set<Int>) = dao.delete(taskIds)
}