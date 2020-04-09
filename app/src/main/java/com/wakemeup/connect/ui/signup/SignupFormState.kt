package com.wakemeup.connect.ui.signup

/**
 * Data validation state of the connect form.
 */
data class SignupFormState(
    val usernameSignupError: Int? = null,
    val passwordSignupError: Int? = null,
    val password2SignupError: Int? = null,
    val mailSignupError: Int? = null,
    val phoneSignupError: Int? = null,
    val isDataValid: Boolean = false
)
