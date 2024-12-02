package com.example.quizai.ui.auth

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
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated
    
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            // TODO: Implement actual authentication
            _isAuthenticated.value = true
        }
    }
    
    fun signOut() {
        viewModelScope.launch {
            // TODO: Implement sign out
            _isAuthenticated.value = false
        }
    }
} 