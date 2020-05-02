package com.wakemeup.connect.ui.EditUser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.*
import com.google.firebase.database.*
import com.google.firebase.firestore.auth.User
import com.wakemeup.AppWakeUp
import com.wakemeup.MainActivity
import com.wakemeup.R
import com.wakemeup.connect.ConnectActivity
import com.wakemeup.connect.UserModel
import kotlinx.android.synthetic.main.activity_edit_user.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_update_email.*

class EditUser : AppCompatActivity() {


    lateinit var currentUserModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        val toolbar = toolbar
        setSupportActionBar(toolbar)

        Log.i("EditUser", AppWakeUp.auth.currentUser!!.email)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val dataSnapshot: DataSnapshot
        val reference =
            AppWakeUp.database.getReference("Users").child(AppWakeUp.auth.currentUser!!.uid)

        reference.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user: UserModel = dataSnapshot.getValue(UserModel::class.java)!!
                    currentUserModel = user


                    valeur_identifiant.text = currentUserModel.username
                    valeur_mdp.text = "*********"


                    Log.i("EditUser", AppWakeUp.auth.currentUser!!.email)
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
            startActivity(
                intent
            )

        } else {
            Log.i("EditUser", "pas d'extra")
            valeur_email.text = AppWakeUp.auth.currentUser!!.email
        }



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
            intent.putExtra("name",currentUserModel.username)
            startActivity(intent)
        }
    }
}