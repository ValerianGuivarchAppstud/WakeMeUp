package com.vguivarc.wakemeup.transport.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.databinding.FragmentAuthBinding
import org.jetbrains.anko.support.v4.longToast
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.orbitmvi.orbit.viewmodel.observe
import timber.log.Timber


class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val viewModel by viewModel<AuthViewModel>()

    private var _binding: FragmentAuthBinding? = null

    private val callbackManager = CallbackManager.Factory.create()

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.fullyInitialize()
        }
        binding.loginClose.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.buttonSeConnecter.setOnClickListener {
            findNavController().navigate(R.id.action_login_email)
        }

        setupFacebookLogic()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observe(this, state = ::render, sideEffect = ::handleSideEffect)
    }

    private fun render(state: AuthState) {
        Timber.d("render $state")
        binding.loader.isVisible = state.isLoading
        if(state.isConnected) {
            activity?.onBackPressed()
        }
    }

    private fun handleSideEffect(sideEffect: AuthSideEffect) {
        when (sideEffect) {
            is AuthSideEffect.Toast -> Toast.makeText(
                context,
                sideEffect.textResource,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun setupFacebookLogic() {

        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.fullyInitialize()
        }

        binding.connecterFb.setOnClickListener {
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
                        viewModel.loginWithFacebook(loginResult.accessToken.token)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
/*
    private fun onLoginFacebookSuccessful() {
        // TODO use google smart lock to save password

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
*/
}

/*
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
*/