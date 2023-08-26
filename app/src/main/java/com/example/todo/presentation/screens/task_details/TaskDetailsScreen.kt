package com.example.todo.presentation.screens.task_details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.todo.R
import com.example.todo.presentation.components.LoadingScreen
import com.example.todo.presentation.components.TaskProperties
import com.example.todo.presentation.components.TopAppBar

@Composable
fun TaskDetailsScreen(
    modifier: Modifier = Modifier,
    screenState: TaskDetailsScreenState,
    changeName: (String) -> Unit,
    changeDescription: (String) -> Unit,
    changeIsCompleted: (Boolean) -> Unit,
    saveChanges: () -> Unit,
    delete: () -> Unit,
    navigateBack: () -> Unit
) = Scaffold(
    modifier = modifier,
    topBar = {
        TopAppBar(
            title = stringResource(id = R.string.task_details),
            navigateBack = navigateBack,
            actions = {
                IconButton(
                    onClick = {
                        delete()
                        navigateBack()
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(27.dp),
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        )
    },
    floatingActionButton = {
        val visible = remember(screenState) {
            derivedStateOf {
                screenState is TaskDetailsScreenState.Success && !screenState.changesSaved
            }
        }

        AnimatedVisibility(visible = visible.value) {
            FloatingActionButton(
                modifier = Modifier.padding(12.dp),
                onClick = saveChanges,
                shape = CircleShape
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_save),
                    contentDescription = stringResource(R.string.save_changes),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    },
    content = {
        when(screenState) {
            is TaskDetailsScreenState.Loading -> LoadingScreen()
            is TaskDetailsScreenState.NotFound -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.not_found),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            is TaskDetailsScreenState.Success -> TaskProperties(
                modifier = Modifier.padding(it),
                name = screenState.task.name,
                invalidName = screenState.invalidName,
                description = screenState.task.description,
                completed = screenState.task.completed,
                onNameChange = changeName,
                onDescriptionChange = changeDescription,
                onIsCompletedChange = changeIsCompleted
            )
        }
    }
)