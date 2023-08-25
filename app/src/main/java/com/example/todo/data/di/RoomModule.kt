package com.example.todo.data.di

import android.content.Context
import androidx.room.Room
import com.example.todo.data.room.TaskDao
import com.example.todo.data.room.ToDoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    @Singleton
    @Provides
    fun provideToDoDatabase(@ApplicationContext appContext: Context): ToDoDatabase =
        Room
        .databaseBuilder(appContext, ToDoDatabase::class.java, ToDoDatabase.NAME)
        .build()

    @Provides
    fun provideTaskDao(db: ToDoDatabase): TaskDao = db.taskDao()
}