package com.example.tidey.domain.repository

import com.example.tidey.domain.model.Response
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun isUserAuthenticatedInFirebase(): Boolean

    suspend fun oneTapSignInWithGoogle(): Flow<Response<BeginSignInResult>>

    suspend fun oneTapSignUpWithGoogle(): Flow<Response<BeginSignInResult>>

    suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): Flow<Response<Boolean>>

    suspend fun signOut(): Flow<Response<Boolean>>

    fun getFirebaseAuthState(): Flow<Boolean>

    fun getDisplayName(): String

    fun getPhotoUrl(): String
}