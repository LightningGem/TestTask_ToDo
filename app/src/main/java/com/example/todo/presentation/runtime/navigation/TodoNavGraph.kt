package com.example.todo.presentation.runtime.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todo.presentation.screens.add_task.AddTaskScreen
import com.example.todo.presentation.screens.add_task.AddTaskViewModel
import com.example.todo.presentation.screens.tasks.TasksScreen
import com.example.todo.presentation.screens.tasks.TasksViewModel
import com.sebaslogen.resaca.hilt.hiltViewModelScoped

@Composable
fun ToDoNavGraph(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Destination.TasksListDetails.route,
        enterTransition = { fadeIn(animationSpec = tween()) },
        exitTransition = { fadeOut(animationSpec = tween()) }
    ) {
        composable(route = Destination.TasksListDetails.route) {
            val viewModel : TasksViewModel = hiltViewModelScoped()
            val screenState = viewModel.screenState.collectAsState()

            TasksScreen(
                screenState = screenState.value,
                openTask = { /*TODO*/ },
                addTask = { navController.navigate(Destination.AddTask.route) },
                deleteTasks = viewModel::deleteTasks,
                changeIsTaskCompleted = viewModel::changeIsTaskCompleted,
                changeSelectedTasks = viewModel::changeSelectedTasks
            )
        }

        composable(route = Destination.AddTask.route) {
            val viewModel : AddTaskViewModel = hiltViewModelScoped()
            val screenState = viewModel.screenState.collectAsState()

            AddTaskScreen(
                screenState = screenState.value,
                changeName = viewModel::changeName,
                changeDescription = viewModel::changeDescription,
                changeIsCompleted = viewModel::changeIsCompleted,
                addTask = viewModel::add,
                navigateBack = { navController.navigateUp() }
            )
        }
    }
}


@Composable
private fun TasksListDetailsScreen(windowSizeClass: WindowSizeClass) {
    TODO()
}