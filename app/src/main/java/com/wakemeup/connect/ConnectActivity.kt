package com.wakemeup.connect

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.wakemeup.AppWakeUp
import com.wakemeup.MainActivity
import com.wakemeup.R
import com.wakemeup.connect.ui.LoginActivity
import com.wakemeup.connect.ui.SignupActivity
import com.wakemeup.notification.MyFirebaseMessagingService
import com.wakemeup.notification.Token
import kotlinx.android.synthetic.main.activity_connect.*


class ConnectActivity : AppCompatActivity() {

    companion object {
        const val CREATION_COMPTE = 1
        const val CONNECTION_COMPTE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)

        main_button_reveil.setOnClickListener {
            val intentCreerCompte = Intent(this, SignupActivity::class.java)
            startActivityForResult(intentCreerCompte, CREATION_COMPTE)
        }


        connect_button_se_connecter.setOnClickListener {
            val intentSeConnecter = Intent(this, LoginActivity::class.java)
            startActivityForResult(intentSeConnecter, CONNECTION_COMPTE)
        }


        connect_button_continuer_sans_compte.setOnClickListener {

            AppWakeUp.auth.signInAnonymously()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        startMainActivityBeingConnected()
                    } else {
                        Log.e("MyApp", "Anonymous login failed.")
                    }
                }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            CREATION_COMPTE -> {
                if (AppWakeUp.auth.currentUser != null) {
                    AppWakeUp.auth.signOut()
                    //startMainActivityBeingConnected()
                }
            }
            CONNECTION_COMPTE -> {
                if (AppWakeUp.auth.currentUser != null) {
                    sayWelcome()
                    startMainActivityBeingConnected()
                }
            }
        }

        // quand on arrive pour la première fois dans main, si pas connecté, ça lance la page de connexion.
        // si on la quitte (cancel) on quitte l'appli
        // si on réussit à se connecter ou qu'on prend un anonyme on revient au main en OK
        // si on clique pour aller sur connect, pareil, mais si on revient en arrière ça quitte pas l'app
        // donc : si on est en cancel et sans user (même pas anonyme), on quitte

    }

    private fun startMainActivityBeingConnected() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnSuccessListener { instanceIdResult ->
                val token = instanceIdResult.token
                MyFirebaseMessagingService.registerInstanceOfUser(
                    Token(token),
                    AppWakeUp.auth.currentUser
                )

                // Do whatever you want with your token now
                // i.e. store it on SharedPreferences or DB
                // or directly send it to server
            }

        val intent = Intent(this@ConnectActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun sayWelcome(){

        val reference =
            AppWakeUp.database.getReference("Users").child(AppWakeUp.auth.currentUser!!.uid)

        reference.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user: UserModel = dataSnapshot.getValue(UserModel::class.java)!!
                    val welcome = getString(R.string.welcome)
                        Toast.makeText(
                            applicationContext,
                            "$welcome ${user.username}",
                            Toast.LENGTH_LONG
                        ).show()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("MainActivity", "loadPost:onCancelled ${databaseError.message}")
                }
            }
        )
    }

}
