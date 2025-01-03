package com.trilliontests.data.repository

// TODO: THIS REPOSITORY WILL STORE USER PREFERENCE DATA
/*

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class PreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val HAS_COMPLETED_ONBOARDING = booleanPreferencesKey("has_completed_onboarding")
        val USER_TYPE = stringPreferencesKey("user_type")
        val STUDY_PURPOSES = stringSetPreferencesKey("study_purposes")
    }

    val userPreferences: Flow<UserPreferences> = context.dataStore.data.map { preferences ->
        UserPreferences(
            hasCompletedOnboarding = preferences[PreferencesKeys.HAS_COMPLETED_ONBOARDING] ?: false,
            userType = preferences[PreferencesKeys.USER_TYPE],
            studyPurposes = preferences[PreferencesKeys.STUDY_PURPOSES] ?: emptySet()
        )
    }

    suspend fun updateOnboardingStatus(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.HAS_COMPLETED_ONBOARDING] = completed
        }
    }

    suspend fun updateUserType(userType: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_TYPE] = userType
        }
    }

    suspend fun updateStudyPurposes(purposes: Set<String>) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.STUDY_PURPOSES] = purposes
        }
    }
}

*/