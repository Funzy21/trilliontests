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
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
} 