package com.trilliontests.data.preferences

data class UserPreferences(
    val hasCompletedOnboarding: Boolean = false,
    val userType: String? = null,
    val studyPurposes: Set<String> = emptySet()
) 