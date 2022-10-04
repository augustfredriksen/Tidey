package com.example.tidey.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tidey.presentation.auth.AuthViewModel
import com.example.tidey.presentation.navigation.Screen
import com.example.tidey.presentation.navigation.SetupNavGraph
import com.example.tidey.presentation.ui.theme.TideyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel by viewModels<AuthViewModel>()
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TideyTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    navController = rememberNavController()
                    SetupNavGraph(navController = navController)
                    getAuthState()
                }
                }
            }
        }

    private fun getAuthState() = authViewModel.getAuthState()
    }

