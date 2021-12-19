package com.vguivarc.wakemeup.transport.old.auth

import android.os.Bundle
import androidx.fragment.app.Fragment

private const val EMAIL_ADDRESS = "email"

/**
 * Creates a bundle containing the prefilled email
 * @param email to prefill
 * @return A bundle containing the email
 */
fun forgotPasswordArgs(email: String): Bundle {
    val bundle = Bundle()
    bundle.putString(EMAIL_ADDRESS, email)
    return bundle
}

class ForgotPasswordFragment : Fragment() {
/*
    private val email: String? by lazy { arguments?.getString(EMAIL_ADDRESS) }

    private val authViewModel: AuthViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as BaseActivity).setSupportActionBar(forgotPasswordToolbar)
        (activity as BaseActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        forgotPasswordEmailInput.setText(email)

        forgotPasswordSubmit.setOnClickListener {
            submit()
        }
        forgotPasswordEmailInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submit()
            }
            false
        }
    }

    private fun resetErrors() {
        forgotPasswordEmailLayout.error = null
    }

    private fun submit() {
        resetErrors()
        val email = forgotPasswordEmailInput.text.toString().trim()

        if (fieldsAreValid(email)) {
            authViewModel.forgotPassword(
                email = email
            ).observe(
                viewLifecycleOwner,
                Observer {
                    when (it) {
                        is Success -> {
                            showContent()
                            resetPasswordSuccessful(email)
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

    private fun fieldsAreValid(email: String): Boolean {
        if (!email.isValidEmail())
            forgotPasswordEmailLayout.error = getString(R.string.form_email_format_error)

        return email.isValidEmail()
    }

    override fun showError(throwable: Throwable?) {
        toast(R.string.error_default_message)
        showContent()
    }

    private fun resetPasswordSuccessful(email: String) {
        showContent()
        alert {
            message = getString(R.string.forgot_password_success, email)
            okButton {
                activity?.onBackPressed()
            }
        }.show()
    }*/
}
