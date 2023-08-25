package com.example.todo.data.di

import com.example.todo.data.TasksRepositoryImpl
import com.example.todo.domain.TasksRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindTasksRepository(impl: TasksRepositoryImpl) : TasksRepository
}