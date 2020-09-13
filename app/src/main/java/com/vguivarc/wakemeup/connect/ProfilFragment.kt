package com.vguivarc.wakemeup.connect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.Profile
import com.facebook.ProfileTracker
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.CurrentUserViewModel
import com.vguivarc.wakemeup.MainActivity
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.connect.ui.login.FbLoginViewModel
import com.vguivarc.wakemeup.repo.ViewModelFactory
import timber.log.Timber


class ProfilFragment : Fragment() {


    companion object {
        const val CREATION_COMPTE = 1
        const val CONNECTION_COMPTE = 2
    }


    private lateinit var currentUserViewModel: CurrentUserViewModel
    private lateinit var fbLoginViewModel: FbLoginViewModel



    lateinit var currentView : View

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

        val fb_login_button = currentView.findViewById<LoginButton>(R.id.fb_login_button)
        fb_login_button.setReadPermissions("email")//TODO FACEBOOK, "user_friends")

        val factory = ViewModelFactory(AppWakeUp.repository)
        fbLoginViewModel =
            ViewModelProvider(this, factory).get(FbLoginViewModel::class.java)

        fbLoginViewModel.getLoginResultLiveData()

        fb_login_button.registerCallback(
            MainActivity.callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Timber.e("facebook:onSuccess")
                    fbLoginViewModel.login(loginResult.accessToken)
                }

                override fun onCancel() {
                    Timber.e("facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Timber.e("facebook:onError" + error)
                }
            })

        currentUserViewModel =
            ViewModelProvider(this, factory).get(CurrentUserViewModel::class.java)


        currentUserViewModel.getCurrentUserLiveData().observe(
            requireActivity(),
            androidx.lifecycle.Observer {
                if (it != null) {
                    currentView.findViewById<ProgressBar>(R.id.loading_profil_fragment).visibility= View.VISIBLE
                    currentView.findViewById<TextView>(R.id.profil_fragment_nom).text = it.username
                    Glide.with(this).load(it.imageUrl)
                        .placeholder(R.drawable.empty_picture_profil)
                        .error(R.drawable.empty_picture_profil)
                        .into(currentView.findViewById(R.id.profil_fragment_picture))
                  //  Glide.with(requireContext()).load(it.imageUrl).into(currentView.findViewById(R.id.profil_fragment_picture))
                    currentView.findViewById<ProgressBar>(R.id.loading_profil_fragment).visibility= View.GONE
                } else {
                    currentView.findViewById<TextView>(R.id.profil_fragment_nom).text =
                        "Utilisateur anonyme"
                    Glide.with(requireContext()).load(R.drawable.empty_picture_profil).into(
                        currentView.findViewById<ImageView>(
                            R.id.profil_fragment_picture
                        )
                    )
                }
            })
        return currentView
    }


}
