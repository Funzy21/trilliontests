package com.example.quizai

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class QuizAIApp : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                Log.d("QuizAIApp", "Initializing Firebase...")
                val app = FirebaseApp.initializeApp(this)
                if (app != null) {
                    Log.d("QuizAIApp", "Firebase initialized successfully")
                } else {
                    Log.e("QuizAIApp", "Firebase initialization returned null")
                }
            } else {
                Log.d("QuizAIApp", "Firebase already initialized")
            }
        } catch (e: Exception) {
            Log.e("QuizAIApp", "Firebase initialization failed", e)
            throw e
        }
    }
}