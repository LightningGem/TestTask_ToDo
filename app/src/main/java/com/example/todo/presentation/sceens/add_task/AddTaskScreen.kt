package com.example.todo.presentation.sceens.add_task

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.todo.R
import com.example.todo.presentation.components.TaskProperties
import com.example.todo.presentation.components.TopAppBar

@Composable
fun AddTaskScreen(
    modifier: Modifier = Modifier,
    screenState: AddTaskScreenState,
    changeName: (String) -> Unit,
    changeDescription: (String) -> Unit,
    changeIsCompleted: (Boolean) -> Unit,
    addTask: () -> Unit,
    navigateBack: () -> Unit
) = Scaffold(
    modifier = modifier,
    topBar = {
        TopAppBar(
            title = stringResource(id = R.string.add_task),
            navigateBack = navigateBack,
            actions = {
                IconButton(addTask) {
                    Icon(
                        modifier = Modifier.size(27.dp),
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.add_task),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        )
    },
    content = {
        if(screenState.added) navigateBack()
        TaskProperties(
            modifier = Modifier.padding(it),
            name = screenState.name,
            invalidName = screenState.invalidName,
            description = screenState.description,
            completed = screenState.completed,
            onNameChange = changeName,
            onDescriptionChange = changeDescription,
            onIsCompletedChange = changeIsCompleted
        )
    }
)