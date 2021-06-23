package com.vguivarc.wakemeup.ui.login

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.base.BaseLceFragment
import com.vguivarc.wakemeup.base.Fail
import com.vguivarc.wakemeup.base.Loading
import com.vguivarc.wakemeup.base.Success
import com.vguivarc.wakemeup.util.isValidEmail
import com.vguivarc.wakemeup.util.isValidPassword
import com.vguivarc.wakemeup.viewmodel.AccountViewModel
import com.vguivarc.wakemeup.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.fragment_auth_login_form.*
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFormFragment : BaseLceFragment(R.layout.fragment_auth_login_form) {

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
    }
}
