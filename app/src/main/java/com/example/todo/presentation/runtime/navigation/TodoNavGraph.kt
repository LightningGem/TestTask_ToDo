package com.example.todo.presentation.runtime.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReusableContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todo.R
import com.example.todo.presentation.screens.add_task.AddTaskScreen
import com.example.todo.presentation.screens.add_task.AddTaskViewModel
import com.example.todo.presentation.screens.task_details.TaskDetailsScreen
import com.example.todo.presentation.screens.task_details.TaskDetailsViewModel
import com.example.todo.presentation.screens.tasks.TasksScreen
import com.example.todo.presentation.screens.tasks.TasksViewModel
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
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
            TasksListDetailsScreen(
                windowSizeClass = windowSizeClass,
                addTask = { navController.navigate(Destination.AddTask.route) }
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
private fun TasksListDetailsScreen(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    addTask: () -> Unit
) {

    val selectedTaskId = rememberSaveable { mutableStateOf<Int?>(null) }

    val tasksViewModel : TasksViewModel = hiltViewModelScoped()

    val taskDetailsViewModel : TaskDetailsViewModel = hiltViewModelScoped(
        key = selectedTaskId.value, defaultArguments = bundleOf(TaskDetailsViewModel.ID_KEY to selectedTaskId.value)
    )

    if(windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded && windowSizeClass.heightSizeClass > WindowHeightSizeClass.Compact) TwoPane(
        modifier = modifier,
        first = {
            val tasksScreenState = tasksViewModel.screenState.collectAsState()
            Row {
                TasksScreen(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false),
                    screenState = tasksScreenState.value,
                    addTask = addTask,
                    changeIsTaskCompleted = tasksViewModel::changeIsTaskCompleted,
                    changeSelectedTasks = tasksViewModel::changeSelectedTasks,
                    deleteTasks = tasksViewModel::deleteTasks,
                    openTask = { selectedTaskId.value = it }
                )
                Divider(
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
            }
        },
        second = {
            Crossfade(targetState = taskDetailsViewModel) {
                if(it.id == null) Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.select_task),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                else ReusableContent(key = it) {
                    val taskDetailsScreenState = it.screenState.collectAsState()

                    TaskDetailsScreen(
                        screenState = taskDetailsScreenState.value,
                        changeName =it::changeName,
                        changeDescription = it::changeDescription,
                        changeIsCompleted = it::changeIsCompleted,
                        saveChanges = it::saveChanges,
                        delete = it::deleteTask,
                        navigateBack = { selectedTaskId.value = null }
                    )
                }
            }
        },
        strategy = HorizontalTwoPaneStrategy(splitFraction = 0.4f),
        displayFeatures = emptyList()
    )

    else {
        BackHandler(enabled = selectedTaskId.value != null) { selectedTaskId.value = null }

        val tasksScreenState = tasksViewModel.screenState.collectAsState()

        Crossfade(modifier = modifier, targetState = taskDetailsViewModel) {
            if(it.id == null) TasksScreen(
                screenState = tasksScreenState.value,
                addTask = addTask,
                changeIsTaskCompleted = tasksViewModel::changeIsTaskCompleted,
                changeSelectedTasks = tasksViewModel::changeSelectedTasks,
                deleteTasks = tasksViewModel::deleteTasks,
                openTask = { selectedTaskId.value = it }
            )

            else {
                val taskDetailsScreenState = it.screenState.collectAsState()

                TaskDetailsScreen(
                    screenState = taskDetailsScreenState.value,
                    changeName =it::changeName,
                    changeDescription = it::changeDescription,
                    changeIsCompleted = it::changeIsCompleted,
                    saveChanges = it::saveChanges,
                    delete = it::deleteTask,
                    navigateBack = { selectedTaskId.value = null }
                )
            }
        }
    }
}