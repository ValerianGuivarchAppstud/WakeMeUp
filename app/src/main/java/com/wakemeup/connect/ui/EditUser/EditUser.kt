package com.wakemeup.connect.ui.EditUser

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User
import com.wakemeup.AppWakeUp
import com.wakemeup.R
import com.wakemeup.connect.UserModel
import kotlinx.android.synthetic.main.activity_edit_user.*
import kotlinx.android.synthetic.main.activity_login.*

class EditUser : AppCompatActivity() {

    lateinit var currentUser : UserModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        val toolbar = toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val dataSnapshot : DataSnapshot
        val reference =
            AppWakeUp.database.getReference("Users").child(AppWakeUp.auth.currentUser!!.uid)

        reference.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user: UserModel = dataSnapshot.getValue(UserModel::class.java)!!
                    currentUser = user
                    valeur_identifiant.text = currentUser.username
                    valeur_mdp.text = "*********"
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("MainActivity", "loadPost:onCancelled ${databaseError.message}")
                }
            }
        )






        /*Log.i("EditUser",AppWakeUp.auth.currentUser!!.email.toString())
        AppWakeUp.auth.currentUser!!.updateEmail("user@example.com")
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("EditUser", "User email address updated.")
                }else{
                    Log.d("EditUser","Fail update email")
                }
            }*/


        button_id.setOnClickListener{
            showUpdateIdDialog()
        }
    }

    private fun showUpdateIdDialog() {
        val updateId = EditIDDialogFragment()
        updateId.listener = object : EditIDDialogFragment.EditIDListener{
            override fun onDialogPositiveClick(newId: String) {
               //TODO update id dans la database
               /* val newPassword = newId

                AppWakeUp.auth.currentUser!!.updatePassword(newPassword).addOnCompleteListener{
                    task ->
                    if(task.isSuccessful){
                        Toast.makeText(this@EditUser, "Succes $newPassword", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@EditUser, "Echec", Toast.LENGTH_SHORT).show()
                    }
                }*/

            }

            override fun onNegativeClick() {

            }
        }
        updateId.show(supportFragmentManager,"UpdateIdFragment")
    }


    fun masqueMDP(lenght : Int){

    }
}


