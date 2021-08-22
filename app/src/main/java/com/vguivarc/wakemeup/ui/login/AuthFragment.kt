package com.vguivarc.wakemeup.ui.login

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.base.BaseLceFragment
import com.vguivarc.wakemeup.base.Fail
import com.vguivarc.wakemeup.base.Loading
import com.vguivarc.wakemeup.base.Success
import com.vguivarc.wakemeup.domain.entity.FacebookMissingMailError
import com.vguivarc.wakemeup.viewmodel.AccountViewModel
import com.vguivarc.wakemeup.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.fragment_auth.*
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AuthFragment : BaseLceFragment() {
    private val callbackManager = CallbackManager.Factory.create()
    private val authViewModel: AuthViewModel by sharedViewModel()
    private val accountViewModel: AccountViewModel by sharedViewModel()

    private var clickOnLogin: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginClose.setOnClickListener {
            activity?.onBackPressed()
        }
        loginEmail.setOnClickListener {
            clickOnLogin = true
            findNavController().navigate(R.id.action_login_email)
        }
        setupFacebookLogic()
        val loginLegals = view.findViewById<TextView>(R.id.loginLegals)
        loginLegals.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.legals_link)))
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        clickOnLogin = false
    }

    private fun setupFacebookLogic() {

        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.fullyInitialize()
        }

        loginFacebook.setOnClickListener {
            clickOnLogin = true
            if (AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut()
            }

            LoginManager.getInstance().logInWithReadPermissions(
                this,
                listOf("email")
            )
        }
        // Callback registration
        LoginManager.getInstance()
            .registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        loginWithFacebook(loginResult.accessToken.token)
                    }

                    override fun onCancel() {
                        longToast(R.string.login_facebook_cancelled)
                    }

                    override fun onError(exception: FacebookException) {
                        longToast(R.string.login_facebook_error)
                    }
                }
            )
    }

    private fun loginWithFacebook(token: String) {
        authViewModel.loginWithFacebook(token).observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        onLoginFacebookSuccessful()
                    }
                    is Loading -> showLoading()
                    is Fail -> {
                        showError(it.error)
                        /*AndroidApplication.mFirebaseCrashlytics.apply {
                            log("Error while creating an account with facebook token")
                            it.error?.let { error ->
                                recordException(error)
                                log("E/FacebookToken: ${error.localizedMessage}")
                            }
                        }*/
                    }
                }
            }
        )
    }

    private fun onLoginFacebookSuccessful() {
        // TODO use google smart lock to save password
        toast(R.string.login_successful)

        accountViewModel.getAndUpdateUserInfo()
            .observe(
                viewLifecycleOwner,
                Observer { resource ->
                    when (resource) {
                        is Success -> {
                            activity?.setResult(Activity.RESULT_OK)
                            activity?.finish()
                        }
                        is Loading -> showLoading()
                        is Fail -> {
                            showContent()
                            toast(R.string.error_default_message)
                        }
                    }
                }
            )
    }

    override fun showError(throwable: Throwable?) {
        if (throwable is FacebookMissingMailError) {
            val dialogView =
                layoutInflater.inflate(R.layout.facebook_login_error_dialog, null, false)
            val dialog = alert {
                customView = dialogView
            }.show()
            dialogView.find<View>(R.id.facebookErrorDialogCTA)
                .setOnClickListener { dialog.dismiss() }
        } else {
            toast(R.string.login_facebook_error)
        }

        showContent()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
