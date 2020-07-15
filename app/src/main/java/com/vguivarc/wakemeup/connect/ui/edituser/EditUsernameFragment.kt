package com.vguivarc.wakemeup.connect.ui.edituser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.connect.UserModel
import kotlinx.android.synthetic.main.fragment_update_username.*

private lateinit var context: Context

class EditUsernameFragment : Fragment() {

    companion object {
        fun newInstance(ctx: Context): EditUsernameFragment {
            context = ctx
            return EditUsernameFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update_username, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_update.setOnClickListener {
            var newName = edit_text_username.text.toString()

            if (newName == "") {
                edit_text_username.error = "Veuillez entrer un nom"
                edit_text_username.requestFocus()
                return@setOnClickListener
            }
//TODO refaire ici
            /*
            val firebaseUser = AppWakeUp.auth.currentUser!!
            val userId = firebaseUser.uid
            val reference = AppWakeUp.repository.database.getReference("Users").child(userId)
            val newUser = UserModel(
                userId,
                "",
                newName
            )
            reference.setValue(newUser)
            Toast.makeText(activity, "Nom d'utilisateur modifié", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, EditUser::class.java)
            startActivity(intent)
*/
        }

    }
}