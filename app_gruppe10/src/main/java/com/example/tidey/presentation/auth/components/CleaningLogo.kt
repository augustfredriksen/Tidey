package com.example.tidey.presentation.auth.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import com.example.tidey.R


@Composable
fun CleaningLogo() {
    val configuration = LocalConfiguration.current
    var aspectRatio: Float = 0f
    aspectRatio = when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            1.6f

        }
        else -> {
            .5f
        }
    }

        // Other wise

    Image(
        painter = painterResource(id = R.drawable.ic_beach_image_dark),
        contentDescription = "Beach cleaning image",
        alignment = Alignment.BottomCenter,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp)
            .aspectRatio(aspectRatio),
    )
}
