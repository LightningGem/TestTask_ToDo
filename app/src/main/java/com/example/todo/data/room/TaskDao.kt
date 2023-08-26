package com.example.todo.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAll(): Flow<List<TaskEntity>>

    @Insert
    suspend fun insert(entity : TaskEntity)

    @Update
    suspend fun update(entity: TaskEntity)

    @Query("DELETE FROM tasks WHERE tasks.task_id in (:ids)")
    suspend fun delete(ids: Set<Int>)
}