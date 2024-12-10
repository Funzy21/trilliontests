package com.example.quizai.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.FirebaseUser

@Singleton
class UserRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth  // Inject Firebase Auth
) {
    suspend fun signInWithEmailPassword(email: String, password: String): Boolean {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user != null
        } catch (e: Exception) {
            false
        }
    }
    
    suspend fun signInWithGoogle(): Boolean {
        // Will implement Google sign-in later
        return false
    }
    
    suspend fun signOut() {
        firebaseAuth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
} 