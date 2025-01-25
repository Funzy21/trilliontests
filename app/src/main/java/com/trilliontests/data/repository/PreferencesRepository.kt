package com.trilliontests.data.repository

// TODO: THIS REPOSITORY WILL STORE USER PREFERENCE DATA

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.trilliontests.model.userdata.UserPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
        val IS_DARK_MODE = booleanPreferencesKey("isDarkMode")
        val LANGUAGE = stringPreferencesKey("language")
        val RECENT_ACTIVITY = stringPreferencesKey("recentActivity")
    }

    val userPreferences: Flow<UserPreferences> = context.dataStore.data.map { preferences ->
        UserPreferences(
            hasCompletedOnboarding = preferences[PreferencesKeys.HAS_COMPLETED_ONBOARDING] ?: false,
            userType = preferences[PreferencesKeys.USER_TYPE],
            studyPurposes = preferences[PreferencesKeys.STUDY_PURPOSES] ?: emptySet(),
            isDarkMode = preferences[PreferencesKeys.IS_DARK_MODE] ?: false,
            language = preferences[PreferencesKeys.LANGUAGE],
            recentActivity = preferences[PreferencesKeys.RECENT_ACTIVITY],
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

    suspend fun updateIsDarkMode(darkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DARK_MODE] = darkMode
        }
    }

    suspend fun updateRecentActivity(recentActivity: List<String>){
        val jsonString = Json.encodeToString(recentActivity) // Serialize recentActivity to JSON
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.RECENT_ACTIVITY] = jsonString
        }
    }
}