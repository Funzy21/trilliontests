package com.trilliontests.model.userdata

import kotlinx.serialization.Serializable

data class UserPreferences(
    // Initially set during onboarding
    val hasCompletedOnboarding: Boolean = false,
    val userType: String? = null,
    val studyPurposes: Set<String> = emptySet(),

    // Manually toggled in settings
    val isDarkMode: Boolean,
    val language: String? = null,

    // Data set automatically
    @Serializable
    val recentActivity: List<String>
) 