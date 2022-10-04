package com.example.tidey.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.tidey.R
import com.example.tidey.common.Constants.GARBAGE_SCREEN
import com.example.tidey.common.Constants.MAP_DETAIL_SCREEN
import com.example.tidey.common.Constants.STATISTIC_SCREEN
import com.example.tidey.presentation.map_details.MapDetailScreen
import com.example.tidey.presentation.navigation.Screen

@Composable
fun RowScope.AddItem(
    screen: Screen,
    currentDestination: NavDestination?,
    navController: NavController
) {
    var string = ""
    BottomNavigationItem(
        label = {
            when(screen.route){
                MAP_DETAIL_SCREEN -> {string = LocalContext.current.getString(R.string.Overview)}
                STATISTIC_SCREEN -> {string = LocalContext.current.getString(R.string.Statistics)}
                GARBAGE_SCREEN -> {string = LocalContext.current.getString(R.string.Garbage)}
            }
            Text(text = string)
        },
        icon = {
            screen.icon?.let { Icon(imageVector = it, contentDescription = "Navigation Icon") }
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = {
            navController.popBackStack()
            navController.navigate(screen.route) {
                launchSingleTop = true
            }
        }
    )
}