package com.trilliontests.model.userdata

import androidx.room.PrimaryKey

data class User(
    @PrimaryKey(autoGenerate = true)
    val userID: Int? = null,
    val userName: String,
    val email: String,
    val preferences: UserPreferences
)

//Gil Git Test
