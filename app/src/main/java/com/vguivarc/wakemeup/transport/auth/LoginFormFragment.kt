package com.vguivarc.wakemeup.transport.auth

import androidx.fragment.app.Fragment
import com.vguivarc.wakemeup.R

class LoginFormFragment : Fragment(R.layout.fragment_auth_login_form) {
/*
    private val authViewModel: AuthViewModel by sharedViewModel()
    private val accountViewModel: AccountViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginForgotPassword.setOnClickListener {
            findNavController().navigate(
                R.id.action_forgot_password,
                forgotPasswordArgs(loginEmailInput.text.toString())
            )
        }

        loginSubmit.setOnClickListener {
            submitLogin()
        }
        loginPasswordInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submitLogin()
            }
            false
        }
    }

    private fun resetErrors() {
        loginEmailLayout.error = null
        loginPasswordLayout.error = null
    }

    private fun submitLogin() {
        resetErrors()
        val email = loginEmailInput.text.toString().trim()
        val password = loginPasswordInput.text.toString()

        if (fieldsAreValid(email, password)) {
            authViewModel.login(
                email = email,
                password = password
            ).observe(
                viewLifecycleOwner,
                Observer {
                    when (it) {
                        is Success -> {
                            onLoginSuccessful()
                        }
                        is Loading -> showLoading()
                        is Fail -> {
                            showError()
                        }
                    }
                }
            )
        }
    }

    private fun onLoginSuccessful() {
        toast(R.string.login_successful)
        accountViewModel.getAndUpdateUserInfo().observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        // TODO process after login
                    }
                }
            }
        )
    }

    private fun fieldsAreValid(email: String, password: String): Boolean {
        if (!email.isValidEmail())
            loginEmailLayout.error = getString(R.string.form_email_format_error)

        if (!password.isValidPassword())
            loginPasswordLayout.error = getString(R.string.form_password_format_error)

        return email.isValidEmail() && password.isValidPassword()
    }

    override fun showError(throwable: Throwable?) {
        toast(R.string.login_error)
        showContent()
    }*/
}
