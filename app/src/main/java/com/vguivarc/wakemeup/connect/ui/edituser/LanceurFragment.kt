package com.vguivarc.wakemeup.connect.ui.edituser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vguivarc.wakemeup.R

class LanceurFragment() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user2)

        val extras: Bundle = intent.extras!!
        val categorie = extras.getString("categorie")
        val name = extras.getString("name")
        when (categorie) {
            "email" -> {
                var fragment = EditEmailFragment.newInstance(this, name!!)
                supportFragmentManager.beginTransaction().replace(R.id.layout_fragment, fragment)
                    .commit()
            }
            "password" -> {
                var fragmentPassword = EditPasswordFragment.newInstance(this)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.layout_fragment, fragmentPassword)
                    .commit()
            }
            "username" -> {
                var fragmentUsername = EditUsernameFragment.newInstance(this)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.layout_fragment, fragmentUsername)
                    .commit()

            }
        }


    }


}