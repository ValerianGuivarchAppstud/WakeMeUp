package com.wakemeup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.auth.User

import com.wakemeup.connect.ui.EditUser.EditUser
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsUser() : Fragment() {


    companion object {
        fun newInstance(ctx: Context): SettingsUser {
            return SettingsUser()
        }
    }





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        view.findViewById<Button>(R.id.button_edit_user).setOnClickListener{
            val intent = Intent(activity, EditUser::class.java)
            startActivity(intent)

        }

        return view
    }





}