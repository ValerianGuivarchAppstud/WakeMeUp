package com.vguivarc.wakemeup.connect.ui.edituser

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vguivarc.wakemeup.R
import kotlinx.android.synthetic.main.fragment_update_email.*

private lateinit var context: Context

class EditEmailFragment(val name: String) : Fragment() {


    companion object {
        fun newInstance(ctx: Context, name: String): EditEmailFragment {
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


        button_authenticate.setOnClickListener {
            val password = edit_text_password.text.toString().trim()

            if (password.isEmpty()) {
                edit_text_password.error = "Mot de passe nécessaire"
                edit_text_password.requestFocus()
                return@setOnClickListener
            }

            progressbar.visibility = View.VISIBLE
            //TODO refaire ici
            /*
            AppWakeUp.auth.currentUser?.let { user ->
                val credential = EmailAuthProvider.getCredential(user.email!!, password)
                user.reauthenticate(credential).addOnCompleteListener { task ->
                    progressbar.visibility = View.GONE
                    when {
                        task.isSuccessful -> {
                            layoutPassword.visibility = View.GONE
                            layoutUpdateEmail.visibility = View.VISIBLE
                        }
                        task.exception is FirebaseAuthInvalidCredentialsException -> {
                            edit_text_password.error = "Invalide Password"
                            edit_text_password.requestFocus()
                        }
                        else -> Toast.makeText(
                            context,
                            "Une erreur est survenue",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }*/
        }

        button_update.setOnClickListener { view ->
            val email = edit_text_email.text.toString().trim()
            if (email.isEmpty()) {
                edit_text_email.error = "Email requis"
                edit_text_email.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edit_text_email.error = "Email invalide"
                edit_text_email.requestFocus()
                return@setOnClickListener
            }
            progressbar.visibility = View.VISIBLE

            //TODO refaire ici
            /*
            AppWakeUp.auth.currentUser?.let { user ->
                progressbar.visibility = View.GONE
                AppWakeUp.auth.currentUser!!.verifyBeforeUpdateEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUser = AppWakeUp.auth.currentUser!!
                            val userId = firebaseUser.uid
                            val reference =
                                AppWakeUp.repository.database.getReference("Users").child(userId)
                            val newUser = UserModel(
                                userId,
                                "",
                                name
                            )
                            reference.setValue(newUser)
                            Toast.makeText(
                                context,
                                "un email de confirmation vous a été envoyé",
                                Toast.LENGTH_SHORT
                            ).show()
                            //retour sur la page d'edit
                            val intent = Intent(activity, ConnectActivity::class.java)
                            intent.putExtra("email", email)
                            startActivity(intent)
                        } else {
                            Toast.makeText(context, "Une erreur est survenue", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
            }*/
        }


    }
}
