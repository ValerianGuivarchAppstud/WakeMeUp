package com.vguivarc.wakemeup.connect.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.connect.ui.signup.SignupViewModel
import com.vguivarc.wakemeup.repo.ViewModelFactory
import com.vguivarc.wakemeup.util.afterTextChanged
import timber.log.Timber

class SignupActivity : AppCompatActivity() {

    private lateinit var signupViewModel: SignupViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_signup)

        val username = findViewById<EditText>(R.id.activity_signup_username)
        val password = findViewById<EditText>(R.id.activity_signup_password)
        val password2 = findViewById<EditText>(R.id.activity_signup_password_again)
        val mail = findViewById<EditText>(R.id.activity_signup_mail)
        val signup = findViewById<Button>(R.id.activity_signup_button)
        val loading = findViewById<ProgressBar>(R.id.activity_signup_loading)


        val factory = ViewModelFactory(AppWakeUp.repository)
        signupViewModel =
            ViewModelProvider(this, factory).get(SignupViewModel::class.java)


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
        })

        signupViewModel.getSignupResultLiveData().observe(this@SignupActivity, Observer {
            val signupResult = it ?: return@Observer
            Timber.e("E")
            loading.visibility = View.GONE
            if (signupResult.error != null) {
                Log.e("fail", getString(signupResult.error))
                showSignupFailed(signupResult.error)
            } else {
                signupViewModel.sendEmailConfirm()
                //updateUiWithUser()
            }
            Timber.e("F")
            setResult(Activity.RESULT_OK)
            //Complete and destroy connect activity once successful
            finish()
        })

        username.afterTextChanged {
            signupViewModel.signupDataChanged(
                username.text.toString(),
                password.text.toString(),
                password2.text.toString(),
                mail.text.toString()
            )
        }

        password2.afterTextChanged {
            signupViewModel.signupDataChanged(
                username.text.toString(),
                password.text.toString(),
                password2.text.toString(),
                mail.text.toString()
            )
        }

        mail.afterTextChanged {
            signupViewModel.signupDataChanged(
                username.text.toString(),
                password.text.toString(),
                password2.text.toString(),
                mail.text.toString()
            )
        }

        password.afterTextChanged {
            signupViewModel.signupDataChanged(
                username.text.toString(),
                password.text.toString(),
                password2.text.toString(),
                mail.text.toString()
            )
        }

        //quand on clic sur le bouton pour valider la creation de son compte
        signup.setOnClickListener {
            loading.visibility = View.VISIBLE
            signupViewModel.signup(
                this,
                username.text.toString(),
                password.text.toString(),
                mail.text.toString()
            )

        }

    }


    private fun showSignupFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

}

