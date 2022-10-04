package com.example.tidey.presentation.auth.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.tidey.R
import com.example.tidey.common.Constants.AUTH_SCREEN

@Composable
fun AuthTopBar() {
    TopAppBar (
        title = {
            Text(
                text = LocalContext.current.getString(R.string.Authentication),
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
            )
        },
        backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
    )
}