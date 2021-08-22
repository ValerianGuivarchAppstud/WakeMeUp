package com.vguivarc.wakemeup.ui.login

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.facebook.*
import com.facebook.login.widget.LoginButton
import com.vguivarc.wakemeup.AndroidApplication
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.base.BaseLceFragment
import com.vguivarc.wakemeup.base.Fail
import com.vguivarc.wakemeup.base.Loading
import com.vguivarc.wakemeup.base.Success
import com.vguivarc.wakemeup.viewmodel.AccountViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProfilFragment : BaseLceFragment(R.layout.profil_fragment) {

    // truc lié à Facebook login, l'ancienne version
    /*private lateinit var callbackManager: CallbackManager

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }*/

    private val accountViewModel: AccountViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        object : ProfileTracker() {
            override fun onCurrentProfileChanged(
                oldProfile: Profile?,
                currentProfile: Profile?
            ) {
                if (currentProfile == null) {
                    // fbLoginViewModel.disconnect()
                }
            }
        }

        //  callbackManager = CallbackManager.Factory.create();
        val fbLoginButton = view.findViewById<LoginButton>(R.id.fb_login_button)
        fbLoginButton.setPermissions("user_friends")
        fbLoginButton.fragment = this

//        callbackManager = CallbackManager.Factory.create()
/*
        fbLoginButton.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    Utility.createSimpleToast("facebook:onSuccess - ${loginResult!!.accessToken.userId}")
                   // fbLoginViewModel.login(loginResult.accessToken)
                }

                override fun onCancel() {
                    Utility.createSimpleToast("facebook:onCancel")
                }

                override fun onError(exception: FacebookException) {
                    Utility.createSimpleToast("facebook:onError - ${exception.message!!}")
                }
            }
        )
*/

        accountViewModel.getAndUpdateUserInfo().observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Loading -> showLoading()
                    is Success -> {
                        it.data?.let { profil ->
                            view.findViewById<TextView>(R.id.profil_fragment_nom).text = profil.nickname
                            Glide.with(AndroidApplication.appContext).load(profil.imageUrl)
                                .placeholder(R.drawable.main_logo)
                                .error(R.drawable.main_logo)
                                .into(view.findViewById(R.id.profil_fragment_picture))
                        } ?: run {
                            showError()
                        }
                    }
                    is Fail -> showError()
                }
            }
        )
    }
}
