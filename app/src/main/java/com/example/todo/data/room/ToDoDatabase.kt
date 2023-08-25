package com.example.todo.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun taskDao() : TaskDao
    companion object {
        const val NAME = "TODO_DB"
    }
}