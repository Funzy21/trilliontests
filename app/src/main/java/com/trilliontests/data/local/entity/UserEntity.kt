package com.trilliontests.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.trilliontests.model.userdata.UserPreferences

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userID: Int? = null,
    val userName: String,
    val email: String,
    val preferences: UserPreferences
)

@Entity(tableName = "user_preferences")
data class UserPreferencesEntity(
    @PrimaryKey val hasCompletedOnboarding: Boolean = false,
    val userType: String? = null,
    val studyPurposes: Set<String> = emptySet(),

    val isDarkMode: Boolean,
    val language: String,

    val recentActivity: List<String>
)