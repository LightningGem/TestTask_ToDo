package com.example.todo.presentation.runtime.navigation

sealed class Destination(val route: String) {
    object TasksListDetails : Destination("tasks_list_detail")
    object AddTask : Destination("add_task")
}
