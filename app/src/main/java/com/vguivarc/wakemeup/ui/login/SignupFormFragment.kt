package com.vguivarc.wakemeup.ui.login

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.base.BaseLceFragment
import com.vguivarc.wakemeup.base.Fail
import com.vguivarc.wakemeup.base.Loading
import com.vguivarc.wakemeup.base.Success
import com.vguivarc.wakemeup.domain.entity.AccountAlreadyExistsError
import com.vguivarc.wakemeup.util.isValidEmail
import com.vguivarc.wakemeup.util.isValidNickname
import com.vguivarc.wakemeup.util.isValidPassword
import com.vguivarc.wakemeup.viewmodel.AccountViewModel
import com.vguivarc.wakemeup.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.fragment_auth_signup_form.*
import org.jetbrains.anko.okButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class SignupFormFragment : BaseLceFragment(R.layout.fragment_auth_signup_form) {
    private val authViewModel: AuthViewModel by sharedViewModel()
    private val accountViewModel: AccountViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signupSubmit.setOnClickListener {
            submit()
        }
        signupPasswordInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submit()
            }
            false
        }
    }

    private fun resetErrors() {
        signupEmailLayout.error = null
        signupPasswordLayout.error = null
    }

    private fun submit() {
        resetErrors()
        val email = signupEmailInput.text.toString().trim()
        val password = signupPasswordInput.text.toString().trim()
        val nickname = signupNicknameInput.text.toString().trim()

        if (fieldsAreValid(email, password, nickname)) {
            authViewModel.signup(
                email = email,
                password = password,
                nickname = nickname
            ).observe(
                viewLifecycleOwner,
                {
                    when (it) {
                        is Success -> {
                            onSignupSuccessful(email, password)
                        }
                        is Loading -> showLoading()
                        is Fail -> {
                            showError(it.error, email, nickname)
                        }
                    }
                }
            )
        }
    }

    private fun onSignupSuccessful(email: String, password: String) {
        authViewModel.login(
            email = email,
            password = password
        ).observe(
            viewLifecycleOwner,
            {
                when (it) {
                    is Success -> {
                        onLoginSuccessful()
                    }
                    is Loading -> showLoading()
                    is Fail -> {
                        Timber.e(it.error)
                        showError(null)
                    }
                }
            }
        )
    }

    private fun onLoginSuccessful() {
        // TODO use google smart lock to save password
        toast(R.string.login_successful)

        accountViewModel.getAndUpdateUserInfo()
            .observe(
                viewLifecycleOwner,
                { resource ->
                    when (resource) {
                        is Success -> {
                            activity?.setResult(Activity.RESULT_OK)
                            activity?.finish()
                        }
                        is Loading -> showLoading()
                        is Fail -> {
                            showContent()
                            toast(R.string.error_default_message)
                            Timber.e(resource.error)
                            resource.error?.let {
                                /*  AndroidApplication.mFirebaseCrashlytics.recordException(
                                      error
                                  )*/
                            }
                        }
                    }
                }
            )
    }

    private fun fieldsAreValid(
        email: String,
        password: String,
        nickname: String
    ): Boolean {
        if (!email.isValidEmail())
            signupEmailLayout.error = getString(R.string.form_email_format_error)

        if (!password.isValidPassword())
            signupPasswordLayout.error = getString(R.string.form_password_format_error)

        if (!nickname.isValidNickname())
            signupNicknameLayout.error = getString(R.string.form_nickname_format_error)

        return email.isValidEmail() && password.isValidPassword() && nickname.isValidNickname()
    }

    fun showError(throwable: Throwable?, email: String? = null, nickname: String? = null) {
        if (throwable is AccountAlreadyExistsError) {
            alert {
                message =
                    getString(R.string.signup_account_already_exists_error_dialog, email, nickname)
                okButton { }
            }.show()
        } else {
            throwable?.let {
             //   AndroidApplication.mFirebaseCrashlytics.log("Signup fail email = $email nickname = $nickname")
               // AndroidApplication.mFirebaseCrashlytics.recordException(it)
            }
            toast(R.string.error_default_message)
        }

        showContent()
    }
}
