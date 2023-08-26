package com.example.todo.presentation.runtime.navigation

sealed class Destination(val route: String) {
    object TasksListDetails : Destination("tasks_and_edit")
    object AddTask : Destination("add_task")
}
