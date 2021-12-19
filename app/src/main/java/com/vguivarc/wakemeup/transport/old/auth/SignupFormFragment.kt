package com.vguivarc.wakemeup.transport.old.auth

import androidx.fragment.app.Fragment
import com.vguivarc.wakemeup.R

class SignupFormFragment : Fragment(R.layout.fragment_auth_signup_form) {
/*    private val authViewModel: AuthViewModel by sharedViewModel()
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

        if (!nickname.isValidUsername())
            signupNicknameLayout.error = getString(R.string.form_username_format_error)

        return email.isValidEmail() && password.isValidPassword() && nickname.isValidUsername()
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
    }*/
}
