package com.vguivarc.wakemeup.connect.ui.login

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val usernameLoginError: Int? = null,
    val passwordLoginError: Int? = null,
    val isDataValid: Boolean = false
)
