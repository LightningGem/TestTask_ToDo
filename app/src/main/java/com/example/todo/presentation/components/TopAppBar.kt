package com.example.todo.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.todo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String,
    navigateBack: (() -> Unit)? = null,
    actions: @Composable() (RowScope.() -> Unit) = {}
) = androidx.compose.material3.TopAppBar(
    title = {
        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            softWrap = true,
            overflow = TextOverflow.Ellipsis
        )
    },
    modifier = Modifier.background(MaterialTheme.colorScheme.surface).padding(horizontal = 10.dp),
    navigationIcon = {
        if(navigateBack != null) IconButton(onClick = navigateBack) {
            Icon(
                modifier = Modifier.size(27.dp),
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    },
    actions = actions,
    colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Transparent,
        titleContentColor = Color.Transparent,
    )
)