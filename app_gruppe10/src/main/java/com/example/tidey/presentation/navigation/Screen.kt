package com.example.tidey.presentation.navigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Water
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.tidey.common.Constants.AUTH_SCREEN
import com.example.tidey.common.Constants.GARBAGE_SCREEN
import com.example.tidey.common.Constants.MAP_DETAIL_SCREEN
import com.example.tidey.common.Constants.MAP_SCREEN
import com.example.tidey.common.Constants.SPLASH_SCREEN
import com.example.tidey.common.Constants.STATISTIC_SCREEN

sealed class Screen(val route: String, val icon: ImageVector? = null) {
    object Splash: Screen(SPLASH_SCREEN)
    object Auth: Screen(AUTH_SCREEN)
    object Map: Screen(MAP_SCREEN)
    object MapDetail: Screen(MAP_DETAIL_SCREEN, Icons.Default.Water)
    object Statistic: Screen(STATISTIC_SCREEN, Icons.Default.ShowChart)
    object Garbage: Screen(GARBAGE_SCREEN, Icons.Default.RestoreFromTrash)
}