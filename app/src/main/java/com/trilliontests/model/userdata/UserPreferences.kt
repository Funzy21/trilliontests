package com.trilliontests.model.userdata

data class UserPreferences(
    // Initially set during onboarding
    val hasCompletedOnboarding: Boolean = false,
    val userType: String? = null,
    val studyPurposes: Set<String> = emptySet(),

    // Manually toggled in settings
    val isDarkMode: Boolean,
    val language: String,

    // Data set automatically
    val recentActivity: List<String>
) 