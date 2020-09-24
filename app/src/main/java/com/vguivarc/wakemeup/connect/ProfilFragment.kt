package com.vguivarc.wakemeup.connect

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.CurrentUserViewModel
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.repo.ViewModelFactory


class ProfilFragment : Fragment() {


    private lateinit var  callbackManager : CallbackManager

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)

    }

    private lateinit var currentUserViewModel: CurrentUserViewModel
    private lateinit var fbLoginViewModel: FbLoginViewModel



    private lateinit var currentView : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currentView =  inflater.inflate(R.layout.profil_fragment, container, false)

       object : ProfileTracker() {
            override fun onCurrentProfileChanged(
                oldProfile: Profile?,
                currentProfile: Profile?
            ) {
                if (currentProfile == null) {
                    fbLoginViewModel.disconnect()
                }
            }
        }

      //  callbackManager = CallbackManager.Factory.create();
        val fbLoginButton = currentView.findViewById<LoginButton>(R.id.fb_login_button)
        fbLoginButton.setPermissions("user_friends")
        fbLoginButton.fragment = this

        callbackManager = CallbackManager.Factory.create()

        fbLoginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                AppWakeUp.makeToastShort("facebook:onSuccess - ${loginResult!!.accessToken.userId}")
                fbLoginViewModel.login(loginResult.accessToken)
            }

            override fun onCancel() {
                AppWakeUp.makeToastShort("facebook:onCancel")
            }

            override fun onError(exception: FacebookException) {
                AppWakeUp.makeToastShort("facebook:onError - ${exception.message!!}")
            }
        })

        val factory = ViewModelFactory(AppWakeUp.repository)
       fbLoginViewModel =
            ViewModelProvider(this, factory).get(FbLoginViewModel::class.java)

        fbLoginViewModel.getLoginResultLiveData()

        currentUserViewModel =
            ViewModelProvider(this, factory).get(CurrentUserViewModel::class.java)


        currentUserViewModel.getCurrentUserLiveData().observe(
            requireActivity(),
            {
                if (it != null) {
                    currentView.findViewById<ProgressBar>(R.id.loading_profil_fragment).visibility =
                        View.VISIBLE
                    currentView.findViewById<TextView>(R.id.profil_fragment_nom).text = it.username
                    Glide.with(AppWakeUp.appContext).load(it.imageUrl)
                        .placeholder(R.drawable.main_logo)
                        .error(R.drawable.main_logo)
                        .into(currentView.findViewById(R.id.profil_fragment_picture))
                    currentView.findViewById<ProgressBar>(R.id.loading_profil_fragment).visibility =
                        View.GONE
                } else {
                    currentView.findViewById<TextView>(R.id.profil_fragment_nom).text =
                        AppWakeUp.appContext.resources.getString(R.string.utilisateur_anonyme)
                    Glide.with(AppWakeUp.appContext).load(R.drawable.main_logo).into(
                        currentView.findViewById(
                            R.id.profil_fragment_picture
                        )
                    )
                }
            })
        return currentView
    }


}
