package com.example.tidey.presentation.trash.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InfoRow(key: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = key.plus(":"),
            color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondary
        )
        Text(
            text = value,
            color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondary
        )
    }
}