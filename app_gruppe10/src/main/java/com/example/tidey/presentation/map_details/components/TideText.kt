package com.example.tidey.presentation.map_details.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TideText(text: String, color: Color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface) {
    Text(
        modifier = Modifier
            .padding(16.dp),
        text = text,
        style = MaterialTheme.typography.h5,
        color = color
    )
}

