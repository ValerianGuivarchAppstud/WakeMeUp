package com.vguivarc.wakemeup.connect

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.CurrentUserViewModel
import com.vguivarc.wakemeup.MainActivity
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.connect.ui.LoginActivity
import com.vguivarc.wakemeup.connect.ui.SignupActivity
import com.vguivarc.wakemeup.connect.ui.login.FbLoginViewModel
import com.vguivarc.wakemeup.repo.ViewModelFactory
import com.vguivarc.wakemeup.util.Utility
import kotlinx.android.synthetic.main.activity_connect.*
import timber.log.Timber


class ConnectActivity : AppCompatActivity() {


    companion object {
        const val CREATION_COMPTE = 1
        const val CONNECTION_COMPTE = 2
    }


    private lateinit var currentUserViewModel: CurrentUserViewModel
    private lateinit var fbLoginViewModel: FbLoginViewModel

    lateinit var  callbackManager : CallbackManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)
        callbackManager = CallbackManager.Factory.create()

        val fb_login_button = findViewById(R.id.fb_login_button) as LoginButton
        fb_login_button.setReadPermissions("email", "public_profile", "user_friends")

        val factory = ViewModelFactory(AppWakeUp.repository)
        fbLoginViewModel =
            ViewModelProvider(this, factory).get(FbLoginViewModel::class.java)

        fbLoginViewModel.getLoginResultLiveData()

        fb_login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                fbLoginViewModel.login(loginResult.accessToken)
            }

            override fun onCancel() {
                Timber.e( "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Timber.e( "facebook:onError"+ error)
            }
        })


        currentUserViewModel =
            ViewModelProvider(this, factory).get(CurrentUserViewModel::class.java)

        creer_son_compte.setOnClickListener {
            val intentCreerCompte = Intent(this, SignupActivity::class.java)
            startActivityForResult(intentCreerCompte, CREATION_COMPTE)
        }

        currentUserViewModel.getCurrentUserLiveData().observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                Utility.createSimpleToast("Bonjour " + it.username + " !")
            }
        })

        connect_button_se_connecter.setOnClickListener {
            val intentSeConnecter = Intent(this, LoginActivity::class.java)
            startActivityForResult(intentSeConnecter, CONNECTION_COMPTE)
        }


        connect_button_continuer_sans_compte.setOnClickListener {

            startMainActivityBeingConnected()
            /*AppWakeUp.auth.signInAnonymously()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        startMainActivityBeingConnected()
                    } else {
                        //TODO anonymous ?
                        startMainActivityBeingConnected()
//                        Log.e("MyApp", "Anonymous login failed.")
                    }
                }*/
        }
        Timber.e("D")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            CREATION_COMPTE -> {

                //TODO déconncter pour forcer às e reconnecte rune fois mail de validation rempli
/*                if (AppWakeUp.auth.currentUser != null) {
                    AppWakeUp.auth.signOut()
                    val intentSeConnecter = Intent(this, LoginActivity::class.java)
                    startActivityForResult(intentSeConnecter, CONNECTION_COMPTE)
                }*/
                startMainActivityBeingConnected()
            }
            CONNECTION_COMPTE -> {
                /*if (AppWakeUp.auth.currentUser != null) {
                    sayWelcome()
                    startMainActivityBeingConnected()
                }*/
                startMainActivityBeingConnected()
            }
        }

        // quand on arrive pour la première fois dans main, si pas connecté, ça lance la page de connexion.
        // si on la quitte (cancel) on quitte l'appli
        // si on réussit à se connecter ou qu'on prend un anonyme on revient au main en OK
        // si on clique pour aller sur connect, pareil, mais si on revient en arrière ça quitte pas l'app
        // donc : si on est en cancel et sans user (même pas anonyme), on quitte

    }

    private fun startMainActivityBeingConnected() {
        val intent = Intent(this@ConnectActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}
