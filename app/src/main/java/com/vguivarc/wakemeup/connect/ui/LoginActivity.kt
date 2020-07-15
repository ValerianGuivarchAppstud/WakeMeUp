package com.vguivarc.wakemeup.connect.ui

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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.connect.ui.login.LoginViewModel
import com.vguivarc.wakemeup.connect.ui.signup.SignupViewModel
import com.vguivarc.wakemeup.repo.ViewModelFactory
import com.vguivarc.wakemeup.util.afterTextChanged
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    // For the facebook connexion
//    lateinit var callbackManager : CallbackManager
//    private val TAG = "FaceBookTAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        Timber.e("LOL-B")

        val identifiantMail = findViewById<EditText>(R.id.activity_login_mail)
        val password = findViewById<EditText>(R.id.activity_login_password)
        val login = findViewById<Button>(R.id.activity_login_button)
        val loading = findViewById<ProgressBar>(R.id.activity_login_loading)
    //    val buttonFacebookLogin = findViewById<LoginButton>(R.id.buttonFacebookLogin)

        val factory = ViewModelFactory(AppWakeUp.repository)
        loginViewModel =
            ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        Timber.e("LOL-C")
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
        Timber.e("LOL-D")

        loginViewModel.getLoginResultLiveData().observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer
            Timber.e("LOL-E")
            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            Timber.e("LOL-F")
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


        // Initialize Facebook Login button
        /*
        callbackManager = CallbackManager.Factory.create()
        buttonFacebookLogin.setReadPermissions("email", "public_profile")
        buttonFacebookLogin.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
                // ...
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
                // ...
            }
        })*/
    }

/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }


    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        AppWakeUp.auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = AppWakeUp.auth.currentUser
                    //todo updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //todo updateUI(null)
                }

                // ...
            }
    }
*/
    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

