package com.vguivarc.wakemeup.connect.ui.edituser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.connect.ConnectActivity
import kotlinx.android.synthetic.main.activity_edit_user.*

class EditUser : AppCompatActivity() {


    lateinit var currentUserModel: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        val toolbar = toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//TODO refaire ici
/*
        val reference =
            AppWakeUp.repository.database.getReference("Users").child(AppWakeUp.auth.currentUser!!.uid)

        reference.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user: UserModel = dataSnapshot.getValue(UserModel::class.java)!!
                    currentUserModel = user

                    valeur_identifiant.text = currentUserModel.username
                    valeur_mdp.text = "*********"
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("MainActivity", "loadPost:onCancelled ${databaseError.message}")
                }
            }
        )



        if (intent.extras != null) {
            val extras = intent.extras
            val categorie = extras!!.getString("email")
            valeur_email.text = categorie
            val intent = Intent(this, ConnectActivity::class.java)
            startActivity(intent)

        } else {
            valeur_email.text = AppWakeUp.auth.currentUser!!.email
        }*/

        button_id.setOnClickListener {
            val intent = Intent(this, LanceurFragment::class.java)
            intent.putExtra("categorie", "username")
            startActivity(intent)
        }

        button_mdp.setOnClickListener{
            val intent = Intent(this, LanceurFragment::class.java)
            intent.putExtra("categorie","password")
            startActivity(intent)
        }

        button_email.setOnClickListener(){
            val intent = Intent(this, LanceurFragment::class.java)
            intent.putExtra("categorie","email")
            intent.putExtra("name",currentUserModel.displayName)
            startActivity(intent)
        }
    }
}