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
                    valeur_email.text = currentUserModel.mail
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("MainActivity", "loadPost:onCancelled ${databaseError.message}")
                }
            }
        )

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
            startActivity(intent)
        }
    }
}
/*

    private fun showUpdateIdDialog() {
        val updateId = EditIDDialogFragment()
        updateId.listener = object : EditIDDialogFragment.EditIDListener{
            override fun onDialogPositiveClick(newUsername: String) {
                  //updateUsername(newUsername)
                updatePassword(newUsername)
            }
            override fun onNegativeClick() {}
        }
        updateId.show(supportFragmentManager,"UpdateIdFragment")
    }

    private fun showUpdateEmailDialog() {
        val updateEmailDialog = EditEmailDialogFragment()
        updateEmailDialog.listener = object : EditEmailDialogFragment.EditEmailListener {
            override fun onDialogPositiveClick(newEmail: String) {
                updateEmail(newEmail)
            }

            override fun onNegativeClick() {}


        }
        updateEmailDialog.show(supportFragmentManager, "UpdateEmailFragment")
    }


    fun updateUsername(newName : String ){

        val firebaseUser = AppWakeUp.auth.currentUser!!
        val userId = firebaseUser.uid
        val reference = AppWakeUp.database.getReference("Users").child(userId)
        val newUser = UserModel(userId, "", firebaseUser.phoneNumber ?: "", newName,firebaseUser.email ?: "")
        reference.setValue(newUser)
        Toast.makeText(this, "Nom d'utilisateur modifié", Toast.LENGTH_SHORT).show()
    }

    fun updateEmail(newEmail : String){

        val firebaseUser = AppWakeUp.auth.currentUser!!
        val userId = firebaseUser.uid
        val reference = AppWakeUp.database.getReference("Users").child(userId)
        var credential = EmailAuthProvider
            .getCredential(firebaseUser.email!!,"julienn")
        firebaseUser?.reauthenticate(credential)
        firebaseUser?.updateEmail(newEmail)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("EditUser", "User email address updated.")
                    valeur_email.text = newEmail
                }else{
                    Log.d("EditUser", "Update email fail")
                }
            }

        /*val newUser = UserModel(userId, "", firebaseUser.phoneNumber ?: "",currentUserModel.username ?: "", newEmail)
        reference.setValue(newUser)
        Toast.makeText(this, "Email modifié", Toast.LENGTH_SHORT).show() */

    }

    fun updatePassword(newPassword : String){

        val firebaseUser = AppWakeUp.auth.currentUser!!
        val userId = firebaseUser.uid
        val reference = AppWakeUp.database.getReference("Users").child(userId)
        var credential = EmailAuthProvider
            .getCredential(firebaseUser.email!!,"mdpmdp")
        firebaseUser?.reauthenticate(credential)

            firebaseUser?.let{ user ->
            user.updatePassword(newPassword).addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    Log.d("EditUser", "User password updated.")
                    Toast.makeText(this, "Mot de passe modifié", Toast.LENGTH_SHORT).show()
                }else{
                    Log.d("EditUser", "Password fail")
                }
            }
        }

    }
}

*/
