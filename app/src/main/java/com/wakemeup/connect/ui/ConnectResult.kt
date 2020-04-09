package com.wakemeup.connect.ui

import com.google.firebase.auth.FirebaseUser

/**
 * Authentication/Registration result : success (user details) or error message.
 */
data class ConnectResult(
    val success: FirebaseUser? = null,
    val error: Int? = null
)
