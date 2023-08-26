package com.example.todo.presentation.sceens.tasks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.todo.R
import com.example.todo.domain.entities.Task
import com.example.todo.presentation.components.LoadingScreen
import com.example.todo.presentation.components.TopAppBar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TasksScreen(
    modifier: Modifier = Modifier,
    screenState: TasksScreenState,
    openTask: (Int) -> Unit,
    addTask: () -> Unit,
    deleteTasks: (Set<Int>) -> Unit,
    changeIsTaskCompleted: (Int, Boolean) -> Unit,
    changeSelectedTasks: (Set<Int>) -> Unit
) = Scaffold(
    modifier = modifier,
    topBar = {
        if(screenState is TasksScreenState.Loaded && screenState.selectedTaskIds.isNotEmpty()) TasksSelectAppBar(
            totalSelected = screenState.selectedTaskIds.size,
            onClearClick = { changeSelectedTasks(emptySet()) },
            onDeleteClick = { deleteTasks(screenState.selectedTaskIds) },
            onSelectAllClick = { changeSelectedTasks(screenState.tasks.map { it.id }.toSet()) }
        )
        else TopAppBar(title = stringResource(id = R.string.app_name))
    },
    floatingActionButton = {
        FloatingActionButton(
            modifier = Modifier.padding(12.dp),
            onClick = addTask,
            shape = CircleShape
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_task),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    },
    content = {
        when(screenState) {
            is TasksScreenState.Loading -> LoadingScreen()

            is TasksScreenState.Loaded -> {
                if(screenState.tasks.isEmpty()) Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_tasks),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                else Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                ) {

                    Text(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .padding(top = 0.dp, bottom = 16.dp)
                            .fillMaxWidth(),
                        text = stringResource(R.string.completed_and_total, screenState.tasks.count { it.completed }, screenState.tasks.size),
                        textAlign = TextAlign.End
                    )

                    Divider(thickness = 1.dp)

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .fillMaxSize()
                    ) {
                        items(screenState.tasks, key = { it.id }) { task ->
                            val isSelected = screenState.selectedTaskIds.contains(task.id)

                            TaskItem(
                                modifier = Modifier.animateItemPlacement(),
                                task = task,
                                isSelected = isSelected,
                                onClick = if (screenState.selectedTaskIds.isNotEmpty()) {
                                    {
                                        changeSelectedTasks(
                                            if (isSelected) screenState.selectedTaskIds - task.id else screenState.selectedTaskIds + task.id
                                        )
                                    }
                                } else {
                                    { openTask(task.id) }
                                },
                                onLongClick = { changeSelectedTasks(screenState.selectedTaskIds + task.id) },
                                changeIsTaskCompleted = { isCompleted -> changeIsTaskCompleted(task.id, isCompleted) }
                            )
                        }
                    }
                }
            }
        }
    }
)

@Composable
private fun TasksSelectAppBar(
    totalSelected: Int,
    onClearClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSelectAllClick: () -> Unit
)  = TopAppBar(
    title = stringResource(R.string.total_selected, totalSelected),
    navigateBack = onClearClick,
    actions = {
        IconButton(onDeleteClick) {
            Icon(
                modifier = Modifier.size(27.dp),
                imageVector = Icons.Rounded.Delete,
                contentDescription = stringResource(R.string.delete),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        IconButton(onSelectAllClick) {
            Icon(
                modifier = Modifier.size(27.dp),
                painter = painterResource(R.drawable.ic_select_all),
                contentDescription = stringResource(R.string.select_all),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TaskItem(
    modifier: Modifier = Modifier,
    task: Task,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    changeIsTaskCompleted: (Boolean) -> Unit,
) = Box(
    modifier = modifier
        .combinedClickable(onClick = onClick, onLongClick = onLongClick)
        .background(color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface)
        .padding(vertical = 8.dp)
        .padding(start = 24.dp, end = 12.dp),
    Alignment.Center
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = task.name,
            maxLines = 1,
            softWrap = true,
            overflow = TextOverflow.Ellipsis,
            style = if(task.completed) MaterialTheme.typography.bodyLarge.copy(textDecoration = TextDecoration.LineThrough) else MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.width(8.dp))

        Checkbox(checked = task.completed, onCheckedChange = changeIsTaskCompleted)
    }
}