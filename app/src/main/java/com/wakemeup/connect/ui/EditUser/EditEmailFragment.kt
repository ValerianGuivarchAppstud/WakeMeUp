package com.wakemeup.connect.ui.EditUser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.util.Patterns
import android.view.FrameMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.wakemeup.AppWakeUp
import com.wakemeup.MainActivity
import com.wakemeup.R
import com.wakemeup.connect.ConnectActivity
import com.wakemeup.connect.UserModel
import com.wakemeup.connect.ui.SignupActivity
import kotlinx.android.synthetic.main.activity_edit_user.*
import kotlinx.android.synthetic.main.fragment_update_email.*
import kotlinx.android.synthetic.main.fragment_update_email.button_update
import kotlinx.android.synthetic.main.fragment_update_email.progressbar
import kotlinx.android.synthetic.main.fragment_update_username.*

private lateinit var context: Context
class EditEmailFragment(val name : String) : Fragment() {


    companion object {
        fun newInstance(ctx : Context, name : String): EditEmailFragment {
            context = ctx
            return EditEmailFragment(name)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutPassword.visibility = View.VISIBLE
        layoutUpdateEmail.visibility = View.GONE


        button_authenticate.setOnClickListener{
            val password =  edit_text_password.text.toString().trim()

            if(password.isEmpty()){
                edit_text_password.error = "Mot de passe nécessaire"
                edit_text_password.requestFocus()
                return@setOnClickListener
            }

            progressbar.visibility = View.VISIBLE
            AppWakeUp.auth.currentUser?.let{ user ->
                val credential = EmailAuthProvider.getCredential(user.email!!, password)
                user.reauthenticate(credential).addOnCompleteListener{ task ->
                    progressbar.visibility = View.GONE
                    when{
                        task.isSuccessful -> {
                            layoutPassword.visibility = View.GONE
                            layoutUpdateEmail.visibility = View.VISIBLE
                        }
                        task.exception is FirebaseAuthInvalidCredentialsException -> {
                            edit_text_password.error = "Invalide Password"
                            edit_text_password.requestFocus()
                        }
                        else -> Toast.makeText(context, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        button_update.setOnClickListener{view ->
            val email = edit_text_email.text.toString().trim()
            if (email.isEmpty()){
                edit_text_email.error="Email requis"
                edit_text_email.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edit_text_email.error="Email invalide"
                edit_text_email.requestFocus()
                return@setOnClickListener
            }
            progressbar.visibility = View.VISIBLE
            AppWakeUp.auth.currentUser?.let { user ->
                progressbar.visibility = View.GONE
                AppWakeUp.auth.currentUser!!.verifyBeforeUpdateEmail(email)
                    .addOnCompleteListener{task ->
                        if(task.isSuccessful){
                            val firebaseUser = AppWakeUp.auth.currentUser!!
                            val userId = firebaseUser.uid
                            val reference = AppWakeUp.database.getReference("Users").child(userId)
                            val newUser = UserModel(
                                userId,
                                "",
                                firebaseUser.phoneNumber ?: "",
                                name,
                                email
                            )
                            reference.setValue(newUser)
                            Toast.makeText(context, "un email de confirmation vous a été envoyé", Toast.LENGTH_SHORT).show()
                            //retour sur la page d'edit
                            val intent = Intent(activity, ConnectActivity::class.java)
                            intent.putExtra("email",email)
                            startActivity(intent)
                        }else{
                            Toast.makeText(context, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
                        }

                    }
            }
        }




    }
}
/* val firebaseUser = AppWakeUp.auth.currentUser!!
                            val userId = firebaseUser.uid
                            val reference = AppWakeUp.database.getReference("Users").child(userId)
                            val newUser = UserModel(
                                userId,
                                "",
                                firebaseUser.phoneNumber ?: "",
                                "test",
                                email
                            )
                            reference.setValue(newUser)
                            val sign : SignupActivity = SignupActivity()
                            sign.sendEmailConfirm()

                            AppWakeUp.auth.currentUser
                            */