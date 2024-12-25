package com.trilliontests

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TrillionTestsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                Log.d("TrillionTestsApp", "Initializing Firebase...")
                val app = FirebaseApp.initializeApp(this)
                if (app != null) {
                    Log.d("TrillionTestsApp", "Firebase initialized successfully")
                } else {
                    Log.e("TrillionTestsApp", "Firebase initialization returned null")
                }
            } else {
                Log.d("TrillionTestsApp", "Firebase already initialized")
            }
        } catch (e: Exception) {
            Log.e("TrillionTestsApp", "Firebase initialization failed", e)
            throw e
        }
    }
}