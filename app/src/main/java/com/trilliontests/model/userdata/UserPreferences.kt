package com.trilliontests.model.userdata

data class UserPreferences(
    val hasCompletedOnboarding: Boolean = false,
    val userType: String? = null,
    val studyPurposes: Set<String> = emptySet()
) 