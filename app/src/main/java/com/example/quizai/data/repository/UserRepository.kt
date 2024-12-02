package com.example.quizai.data.repository

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor() {
    suspend fun signInWithEmailPassword(email: String, password: String): Boolean {
        // Will implement Firebase authentication later
        return false
    }
    
    suspend fun signInWithGoogle(): Boolean {
        // Will implement Google sign-in later
        return false
    }
    
    suspend fun signOut() {
        // Will implement sign out later
    }
} 