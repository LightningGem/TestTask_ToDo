package com.example.todo.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.todo.R

@Composable
fun TaskProperties(
    modifier: Modifier = Modifier,
    name: String,
    invalidName: Boolean,
    description: String,
    completed: Boolean,
    onNameChange : (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onIsCompletedChange: (Boolean) -> Unit
) = Column(
    modifier = modifier
        .fillMaxSize()
        .padding(horizontal = 12.dp)
        .padding(top = 12.dp)
) {
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
        errorBorderColor = Color.Transparent
    )

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = name,
        onValueChange = onNameChange,
        isError = invalidName,
        placeholder = {
            Text(
                text = stringResource(R.string.task_name),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Gray
            )
        },
        supportingText = {
            AnimatedVisibility(visible = invalidName) {
                Text(
                    text = stringResource(R.string.invalid_task_name),
                    color = MaterialTheme.colorScheme.error,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )
                Spacer(modifier = Modifier.height(36.dp))
            }
        },
        textStyle = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Normal),
        singleLine = true,
        colors = textFieldColors
    )

    Divider(modifier = Modifier.padding(vertical = 6.dp), thickness = 1.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(checked = completed, onCheckedChange = onIsCompletedChange)

        Spacer(modifier = Modifier.width(12.dp))

        Text(stringResource(id = R.string.task_completed))
    }

    Divider(modifier = Modifier.padding(vertical = 6.dp), thickness = 1.dp)

    OutlinedTextField(
        modifier = Modifier
            .weight(1f, fill = false)
            .fillMaxSize(),
        value = description,
        onValueChange = onDescriptionChange,
        placeholder = {
            Text(
                text = stringResource(id = R.string.task_description),
                color = Color.Gray,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        textStyle = MaterialTheme.typography.titleLarge,
        colors = textFieldColors
    )
}