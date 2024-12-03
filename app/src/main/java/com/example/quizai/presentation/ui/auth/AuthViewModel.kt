package com.example.quizai.presentation.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizai.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
} 