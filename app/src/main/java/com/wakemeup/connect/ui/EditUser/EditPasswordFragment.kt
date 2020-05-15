package com.wakemeup.connect.ui.EditUser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.wakemeup.AppWakeUp
import com.wakemeup.R
import kotlinx.android.synthetic.main.fragment_update_email.*
import kotlinx.android.synthetic.main.fragment_update_password.*
import kotlinx.android.synthetic.main.fragment_update_password.button_authenticate
import kotlinx.android.synthetic.main.fragment_update_password.progressbar

private lateinit var context: Context

class EditPasswordFragment : Fragment() {

    companion object {
        fun newInstance(ctx: Context): EditPasswordFragment {
            context = ctx
            return EditPasswordFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutPasswordPassword.visibility = View.VISIBLE
        layoutUpdatePassword.visibility = View.GONE


        button_authenticate.setOnClickListener {
            val password = edit_text_Oldpassword.text.toString().trim()

            if (password.isEmpty()) {
                edit_text_Oldpassword.error = "Mot de passe nécessaire"
                edit_text_Oldpassword.requestFocus()
                return@setOnClickListener
            }
            progressbar.visibility = View.VISIBLE
            AppWakeUp.auth.currentUser?.let { user ->
                val credential = EmailAuthProvider.getCredential(user.email!!, password)
                user.reauthenticate(credential).addOnCompleteListener { task ->
                    progressbar.visibility = View.GONE
                    when {
                        task.isSuccessful -> {
                            layoutPasswordPassword.visibility = View.GONE
                            layoutUpdatePassword.visibility = View.VISIBLE
                        }
                        task.exception is FirebaseAuthInvalidCredentialsException -> {
                            edit_text_Oldpassword.error = "Mot de passe invalide"
                            edit_text_Oldpassword.requestFocus()
                        }
                        else -> Toast.makeText(
                            context,
                            "Une erreur est survenue",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        button_update_password.setOnClickListener { view ->
            val password1 = edit_password.text.toString().trim()
            val password2 = edit_password2.text.toString().trim()

            if (password1.isEmpty()) {
                edit_password.error = "Mot de passe vide"
                edit_password.requestFocus()
                return@setOnClickListener
            }
            if (password2.isEmpty()) {
                edit_password2.error = "Confirmez le nouveau mot de passe"
                edit_password2.requestFocus()
                return@setOnClickListener
            }
            if (password1.length < 6) {
                edit_password.error = "Le mot de passe doit contenir au moins 6 caractères"
                edit_password.requestFocus()
                return@setOnClickListener
            }
            if (password1 != password2) {
                edit_password.error = "Mots de passe différents"
                edit_password.requestFocus()
                return@setOnClickListener
            }
            progressbar.visibility = View.VISIBLE
            AppWakeUp.auth.currentUser?.let { user ->
                progressbar.visibility = View.GONE
                user.updatePassword(password1)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Mot de passe modifié", Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(activity, EditUser::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(context, "Une erreur est survenue", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
            }
        }


    }
}