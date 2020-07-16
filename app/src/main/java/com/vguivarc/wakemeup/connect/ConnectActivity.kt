package com.vguivarc.wakemeup.connect

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.CurrentUserViewModel
import com.vguivarc.wakemeup.MainActivity
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.connect.ui.LoginActivity
import com.vguivarc.wakemeup.connect.ui.SignupActivity
import com.vguivarc.wakemeup.repo.ViewModelFactory
import com.vguivarc.wakemeup.util.Utility
import kotlinx.android.synthetic.main.activity_connect.*
import timber.log.Timber
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class ConnectActivity : AppCompatActivity() {


    companion object {
        const val CREATION_COMPTE = 1
        const val CONNECTION_COMPTE = 2
    }


    private lateinit var currentUserViewModel: CurrentUserViewModel

    lateinit var  callbackManager : CallbackManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)
        callbackManager = CallbackManager.Factory.create()


        val loginButton = findViewById(R.id.login_button) as LoginButton
        loginButton.setReadPermissions("email", "public_profile", "user_friends")

        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Timber.e( "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Timber.e( "facebook:onCancel")
                // ...
            }

            override fun onError(error: FacebookException) {
                Timber.e( "facebook:onError"+ error)
                // ...
            }
        })


        val factory = ViewModelFactory(AppWakeUp.repository)
        currentUserViewModel =
            ViewModelProvider(this, factory).get(CurrentUserViewModel::class.java)

        main_button_reveil.setOnClickListener {
            val intentCreerCompte = Intent(this, SignupActivity::class.java)
            startActivityForResult(intentCreerCompte, CREATION_COMPTE)
        }

        currentUserViewModel.getCurrentUserLiveData().observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                Utility.createSimpleToast("Bonjour " + it.displayName + " !")
            }
        })

        connect_button_se_connecter.setOnClickListener {
            val intentSeConnecter = Intent(this, LoginActivity::class.java)
            Timber.e("H")
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

    private fun handleFacebookAccessToken(token: AccessToken?) {
        if(token==null){
            Timber.e("null")
        } else {
            Timber.e(token.userId)
            Timber.e(token.permissions.toString())
        }

        Timber.e( "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token!!.token)

        AppWakeUp.repository.signInWithCredential(credential)
        for(p in token.permissions){
            Timber.e("perm="+p)
        }
        AppWakeUp.repository.token=token


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
