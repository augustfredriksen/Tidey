package com.example.tidey.data.repository


import com.example.tidey.common.Constants.SIGN_IN_REQUEST
import com.example.tidey.common.Constants.SIGN_UP_REQUEST
import com.example.tidey.domain.model.Response
import com.example.tidey.domain.repository.AuthRepository
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl  @Inject constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    @Named(SIGN_IN_REQUEST)
    private var signInRequest: BeginSignInRequest,
    @Named(SIGN_UP_REQUEST)
    private var signUpRequest: BeginSignInRequest,
    private var signInClient: GoogleSignInClient,
) : AuthRepository {
    override fun isUserAuthenticatedInFirebase() = auth.currentUser != null

    override suspend fun oneTapSignInWithGoogle() = flow {
        try {
            emit(Response.Loading)
            val result = oneTapClient.beginSignIn(signInRequest).await()
            emit(Response.Success(result))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun oneTapSignUpWithGoogle() = flow {
        try {
            emit(Response.Loading)
            val result = oneTapClient.beginSignIn(signUpRequest).await()
            emit(Response.Success(result))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential) = flow {
        try {
            emit(Response.Loading)
            val authResult = auth.signInWithCredential(googleCredential).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser
            emit(Response.Success(isNewUser))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }


    override fun getFirebaseAuthState() = callbackFlow  {
        val authStateListener = AuthStateListener { auth ->
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    override suspend fun signOut() = flow {
        try {
            emit(Response.Loading)
            auth.signOut()
            oneTapClient.signOut().await()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override fun getDisplayName() = auth.currentUser?.displayName.toString()

    override fun getPhotoUrl() = auth.currentUser?.photoUrl.toString()
}