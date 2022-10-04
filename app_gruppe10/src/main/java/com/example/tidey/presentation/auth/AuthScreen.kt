package com.example.tidey.presentation.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tidey.domain.model.Response.*
import com.example.tidey.presentation.auth.components.AuthTopBar
import com.example.tidey.presentation.auth.components.CleaningLogo
import com.example.tidey.presentation.auth.components.SignInButton
import com.example.tidey.presentation.navigation.Screen
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.example.tidey.presentation.components.ProgressBar


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            AuthTopBar()
        },
    backgroundColor = Color.White
    )
    {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CleaningLogo()
            SignInButton {
                viewModel.oneTapSignIn()
            }

        }
    }


    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                try {
                    val credentials =
                        viewModel.oneTapClient.getSignInCredentialFromIntent(result.data)
                    val googleIdToken = credentials.googleIdToken
                    val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
                    viewModel.signInWithGoogle(googleCredentials)
                } catch (e: ApiException) {
                    Log.d("Firebase", e.toString())
                }
            }
        }

    fun launch(signInResult: BeginSignInResult) {
        val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
        launcher.launch(intent)
    }

    when (val oneTapSignInResponse = viewModel.oneTapSignInState.value) {
        is Loading -> ProgressBar()
        is Success -> {
            oneTapSignInResponse.data?.let {
                LaunchedEffect(it) {
                    launch(it)
                }
            }
        }
        is Failure -> {
            oneTapSignInResponse.e?.let {
                LaunchedEffect(Unit) {
                    print(it)
                    Log.d("Firebase", it.toString())
                    viewModel.oneTapSignUp()
                }
            }
        }
    }

    when (val oneTapSignUpResponse = viewModel.oneTapSignUpState.value) {
        is Loading -> ProgressBar()
        is Success -> {
            oneTapSignUpResponse.data?.let {
                LaunchedEffect(it) {
                    launch(it)
                }
            }
        }
        is Failure -> oneTapSignUpResponse.e?.let {
            LaunchedEffect(Unit) {
                print(it)
                Log.d("Firebase", it.toString())
            }
        }
    }

    when (val signInResponse = viewModel.signInState.value) {
        is Loading -> ProgressBar()
        is Success -> {
            signInResponse.data?.let { isNewUser ->
                if (isNewUser) {

                } else {
                    if (viewModel._signedIn.value == false) {
                        navController.popBackStack()
                        navController.navigate(Screen.Map.route)
                        viewModel._signedIn.value = true
                    }

                }
            }
        }
        is Failure -> signInResponse.e?.let {
            LaunchedEffect(Unit) {
                print(it)
            }
        }
    }

    when (val createUserResponse = viewModel.createUserState.value) {
        is Loading -> ProgressBar()
        is Success -> {
            createUserResponse.data?.let { isUserCreated ->
                if (isUserCreated) {
                    navController.popBackStack()
                    navController.navigate(Screen.Map.route)
                    Log.d("Firebase", "Det funker2")
                }
            }
        }
        is Failure -> createUserResponse.e?.let {
            LaunchedEffect(Unit) {
                print(it)
                Log.d("Firebase", it.toString())
            }
        }
    }
}
