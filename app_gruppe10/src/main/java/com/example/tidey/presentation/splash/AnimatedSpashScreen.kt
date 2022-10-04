package com.example.tidey.presentation.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.tidey.R
import com.example.tidey.presentation.auth.AuthViewModel
import com.example.tidey.presentation.navigation.Screen
import com.example.tidey.presentation.theme.*
import com.example.tidey.presentation.ui.theme.firaSansFamily
import kotlinx.coroutines.delay


@Composable
fun AnimatedSplashScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var startAnimation by remember { mutableStateOf(false)}
    val alphaAnim = animateFloatAsState(
        targetValue = if(startAnimation) 1f else 0f,
    animationSpec = tween(
        durationMillis = 3000
    )
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(4000)
        if (viewModel.isUserAuthenticated) {
            navController.popBackStack()
            navController.navigate(Screen.Map.route)
        }
        else {
            navController.popBackStack()
            navController.navigate(Screen.Auth.route)
        }

    }
    Splash(alpha = alphaAnim.value)
}

@Composable
fun Splash(alpha: Float) {
    Column(modifier = Modifier
        .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Image(
                modifier = Modifier
                    .size(225.dp)
                    .alpha(alpha = alpha),
                painter = painterResource(id = R.drawable.ic_realtidylogo_02),
                contentDescription = "Logo icon")

            Text(
                text = "TIDEY",
                style = MaterialTheme.typography.h2,
                fontFamily = firaSansFamily,
                color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .alpha(alpha = alpha))
        }

        }
    }



