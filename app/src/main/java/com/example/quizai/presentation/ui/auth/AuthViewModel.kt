package com.example.quizai.presentation.ui.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizai.data.repository.UserRepository
import com.example.quizai.utils.GoogleSignInUtils
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    init {
        // Initialize current user
        _currentUser.value = userRepository.getCurrentUser()
        _isAuthenticated.value = _currentUser.value != null
    }

    fun signInWithEmailPassword(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val success = userRepository.signInWithEmailPassword(email, password)
                _isAuthenticated.value = success
                _authState.value = if (success) AuthState.Success else AuthState.Error("Invalid credentials")
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Authentication failed")
            }
        }
    }

    /*
    SHELVING THIS METHOD FOR NOW

    fun signInWithGoogle() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                // Will implement actual Google sign-in later
                _authState.value = AuthState.Error("Google sign-in not implemented yet")
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Google sign-in failed")
            }
        }
    }

     */

    // TODO: Implement sign out behavior
    fun signOut() {
        viewModelScope.launch {
            userRepository.signOut()
            _isAuthenticated.value = false
            _authState.value = AuthState.Idle
        }
    }

    fun handleGoogleSignIn(
        context: Context,
        scope: CoroutineScope,
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
        onSignInSuccess: () -> Unit
    ) {
        GoogleSignInUtils.doGoogleSignIn(
            context = context,
            scope = scope,
            launcher = launcher,
            login = {
                viewModelScope.launch {
                    _isAuthenticated.value = true
                    _authState.value = AuthState.Success
                    onSignInSuccess()
                }
            }
        )
    }

    fun getCurrentUserProfile(): UserProfile? {
        return userRepository.getCurrentUser()?.let { user ->
            UserProfile(
                displayName = user.displayName ?: "",
                email = user.email ?: "",
                photoUrl = user.photoUrl?.toString()
            )
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

data class UserProfile(
    val displayName: String,
    val email: String,
    val photoUrl: String?
) 