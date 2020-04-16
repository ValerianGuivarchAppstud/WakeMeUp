package com.wakemeup.connect.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.wakemeup.AppWakeUp
import com.wakemeup.R
import com.wakemeup.connect.ui.login.LoginViewModel
import com.wakemeup.connect.ui.login.LoginViewModelFactory
import com.wakemeup.util.afterTextChanged

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val identifiantMail = findViewById<EditText>(R.id.activity_login_mail)
        val password = findViewById<EditText>(R.id.activity_login_password)
        val login = findViewById<Button>(R.id.activity_login_button)
        val loading = findViewById<ProgressBar>(R.id.activity_login_loading)

        loginViewModel = ViewModelProviders.of(
            this,
            LoginViewModelFactory()
        )
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable connect button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameLoginError != null) {
                identifiantMail.error = getString(loginState.usernameLoginError)
            }
            if (loginState.passwordLoginError != null) {
                password.error = getString(loginState.passwordLoginError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            } else {
                updateUiWithUser()
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy connect activity once successful
            finish()
        })

        identifiantMail.afterTextChanged {
            loginViewModel.loginDataChanged(
                identifiantMail.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    identifiantMail.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            identifiantMail.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(
                    identifiantMail.text.toString(),
                    password.text.toString()
                )
            }
        }
    }

    private fun updateUiWithUser() {

        val welcome = getString(R.string.welcome)
        val displayName = AppWakeUp.auth.currentUser!!.displayName
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

