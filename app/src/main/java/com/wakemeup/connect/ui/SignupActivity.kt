package com.wakemeup.connect.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.wakemeup.connect.ui.signup.SignupViewModel
import com.wakemeup.connect.ui.signup.SignupViewModelFactory
import com.wakemeup.util.afterTextChanged

class SignupActivity : AppCompatActivity() {

    private lateinit var signupViewModel: SignupViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_signup)

        val username = findViewById<EditText>(R.id.activity_signup_username)
        val password = findViewById<EditText>(R.id.activity_signup_password)
        val password2 = findViewById<EditText>(R.id.activity_signup_password_again)
        val mail = findViewById<EditText>(R.id.activity_signup_mail)
        val phone = findViewById<EditText>(R.id.activity_signup_phone)
        val signup = findViewById<Button>(R.id.activity_signup_button)
        val loading = findViewById<ProgressBar>(R.id.activity_signup_loading)

        signupViewModel = ViewModelProviders.of(
            this,
            SignupViewModelFactory()
        ).get(SignupViewModel::class.java)

        signupViewModel.signupFormState.observe(this@SignupActivity, Observer {
            val signupState = it ?: return@Observer

            // disable connect button unless both username / password is valid
            signup.isEnabled = signupState.isDataValid

            if (signupState.usernameSignupError != null) {
                username.error = getString(signupState.usernameSignupError)
            }
            if (signupState.passwordSignupError != null) {
                password.error = getString(signupState.passwordSignupError)
            }
            if (signupState.password2SignupError != null) {
                password2.error = getString(signupState.password2SignupError)
            }
            if (signupState.mailSignupError != null) {
                mail.error = getString(signupState.mailSignupError)
            }
            if (signupState.phoneSignupError != null) {
                phone.error = getString(signupState.phoneSignupError)
            }
        })

        signupViewModel.signupResult.observe(this@SignupActivity, Observer {
            val signupResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (signupResult.error != null) {
                showSignupFailed(signupResult.error)
            } else {
                sendEmailConfirm()
                //updateUiWithUser()
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy connect activity once successful
            finish()
        })

        username.afterTextChanged {
            signupViewModel.signupDataChanged(
                username.text.toString(),
                password.text.toString(),
                password2.text.toString(),
                mail.text.toString(),
                phone.text.toString()
            )
        }

        password2.afterTextChanged {
            signupViewModel.signupDataChanged(
                username.text.toString(),
                password.text.toString(),
                password2.text.toString(),
                mail.text.toString(),
                phone.text.toString()
            )
        }

        mail.afterTextChanged {
            signupViewModel.signupDataChanged(
                username.text.toString(),
                password.text.toString(),
                password2.text.toString(),
                mail.text.toString(),
                phone.text.toString()
            )
        }

        phone.afterTextChanged {
            signupViewModel.signupDataChanged(
                username.text.toString(),
                password.text.toString(),
                password2.text.toString(),
                mail.text.toString(),
                phone.text.toString()
            )
        }

        password.afterTextChanged {
            signupViewModel.signupDataChanged(
                username.text.toString(),
                password.text.toString(),
                password2.text.toString(),
                mail.text.toString(),
                phone.text.toString()
            )
        }

        //quand on clic sur le bouton pour valider la creation de son compte
        signup.setOnClickListener {
            loading.visibility = View.VISIBLE
            signupViewModel.signup(
                this,
                username.text.toString(),
                password.text.toString(),
                mail.text.toString(),
                phone.text.toString()
            )

        }

    }


    private fun updateUiWithUser() {
        val welcome = getString(R.string.welcome)
        val displayName = "todo"
        //AppWakeUp.auth.currentUser.
        //AppWakeUp.auth.currentUser!!
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showSignupFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    private fun sendEmailConfirm(){
        val user = AppWakeUp.auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Un email de confirmation vous a été envoyé ;)",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

}

