package com.example.tidey.presentation.map_details.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tidey.R
import com.example.tidey.common.Constants

@Composable
fun StatisticsButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(bottom = 48.dp),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
        ),
        border = BorderStroke(0.5.dp, Color.LightGray)
    ) {

        Text(
            text = LocalContext.current.getString(R.string.Statistics),
            modifier = Modifier.padding(8.dp),
            fontSize = 18.sp,
            color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
        )
    }
}