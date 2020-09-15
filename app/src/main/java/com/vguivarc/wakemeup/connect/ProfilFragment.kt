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
import timber.log.Timber


class ProfilFragment : Fragment() {


    lateinit var  callbackManager : CallbackManager

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.e("lol")
        callbackManager.onActivityResult(requestCode, resultCode, data)
        Timber.e("lol2")

    }

    private lateinit var currentUserViewModel: CurrentUserViewModel
    private lateinit var fbLoginViewModel: FbLoginViewModel



    private lateinit var currentView : View
    //private lateinit var callbackManager : CallbackManager

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
        val fb_login_button = currentView.findViewById<LoginButton>(R.id.fb_login_button)
        fb_login_button.setPermissions("email")//TODO FACEBOOK, "user_friends")
        fb_login_button.setFragment(this)

        callbackManager = CallbackManager.Factory.create()

        fb_login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
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
            androidx.lifecycle.Observer {
                if (it != null) {
                    currentView.findViewById<ProgressBar>(R.id.loading_profil_fragment).visibility =
                        View.VISIBLE
                    currentView.findViewById<TextView>(R.id.profil_fragment_nom).text = it.username
                    Glide.with(this).load(it.imageUrl)
                        .placeholder(R.drawable.empty_picture_profil)
                        .error(R.drawable.empty_picture_profil)
                        .into(currentView.findViewById(R.id.profil_fragment_picture))
                    //  Glide.with(requireContext()).load(it.imageUrl).into(currentView.findViewById(R.id.profil_fragment_picture))
                    currentView.findViewById<ProgressBar>(R.id.loading_profil_fragment).visibility =
                        View.GONE
                } else {
                    currentView.findViewById<TextView>(R.id.profil_fragment_nom).text =
                        AppWakeUp.appContext.resources.getString(R.string.utilisateur_anonyme)
                    Glide.with(requireContext()).load(R.drawable.empty_picture_profil).into(
                        currentView.findViewById(
                            R.id.profil_fragment_picture
                        )
                    )
                }
            })
        return currentView
    }


}
