package com.example.tidey.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tidey.presentation.auth.AuthScreen
import com.example.tidey.presentation.map.MapScreen
import com.example.tidey.presentation.map.MapViewModel
import com.example.tidey.presentation.map_details.MapDetailScreen
import com.example.tidey.presentation.splash.AnimatedSplashScreen
import com.example.tidey.presentation.statistic.StatisticScreen
import com.example.tidey.presentation.trash.GarbageScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    val sharedViewModel: MapViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            AnimatedSplashScreen(navController)
        }

        composable(route = Screen.Auth.route) {
            AuthScreen(navController)
        }

        composable(route = Screen.Map.route) {
            MapScreen(navController, sharedViewModel)
        }

        composable(route = Screen.MapDetail.route) {
            MapDetailScreen(navController, sharedViewModel)
        }
        
        composable(route = Screen.Statistic.route) {
            StatisticScreen(navController, sharedViewModel)
        }

        composable(route = Screen.Garbage.route) {
            GarbageScreen(navController, sharedViewModel)
        }
    }
}