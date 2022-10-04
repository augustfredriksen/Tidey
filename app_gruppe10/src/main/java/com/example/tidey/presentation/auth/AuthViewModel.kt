package com.example.tidey.presentation.auth

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.tidey.domain.model.Response
import com.example.tidey.domain.model.Response.*
import com.example.tidey.domain.repository.AuthRepository
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository,
    val oneTapClient: SignInClient
) : ViewModel() {
    val isUserAuthenticated get() = repo.isUserAuthenticatedInFirebase()

    private val _oneTapSignInState = mutableStateOf<Response<BeginSignInResult>>(
        Success(
            null
        )
    )
    val oneTapSignInState: State<Response<BeginSignInResult>> = _oneTapSignInState

    private val _oneTapSignUpState = mutableStateOf<Response<BeginSignInResult>>(
        Success(
            null
        )
    )
    val oneTapSignUpState: State<Response<BeginSignInResult>> = _oneTapSignUpState

    private val _signInState = mutableStateOf<Response<Boolean>>(Success(null))
    val signInState: State<Response<Boolean>> = _signInState

    var _signedIn = MutableLiveData(false)

    private val _createUserState = mutableStateOf<Response<Boolean>>(Success(null))
    val createUserState: State<Response<Boolean>> = _createUserState

    fun getAuthState() = liveData(Dispatchers.IO) {
        repo.getFirebaseAuthState().collect { response ->
            emit(response)
        }
    }

    fun oneTapSignIn() {
        viewModelScope.launch {
            repo.oneTapSignInWithGoogle().collect { response ->
                _oneTapSignInState.value = response
                Log.d("Firebase", "oneTapSignIn $response")
            }
        }
    }

    fun oneTapSignUp() {
        viewModelScope.launch {
            repo.oneTapSignUpWithGoogle().collect { response ->
                _oneTapSignUpState.value = response
            }
        }
    }

    fun signInWithGoogle(googleCredential: AuthCredential) {
        viewModelScope.launch {
            repo.firebaseSignInWithGoogle(googleCredential).collect { response ->
                _signInState.value = response
                Log.d("Firebase", "signInWithGoogle $response")
            }
        }
    }

}
