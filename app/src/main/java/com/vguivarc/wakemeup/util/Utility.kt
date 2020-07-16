package com.vguivarc.wakemeup.util

import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.vguivarc.wakemeup.AppWakeUp


object Utility {

    fun convertDuration(duration: Int): String {

        val minutes = duration / 60
        val seconds = duration % 60

        return String.format("%d:%02d", minutes, seconds)
    }

    fun createSimpleToast(text : String){
        Toast.makeText(
            AppWakeUp.appContext,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    class UserModel(val uid : String ?, val name : String?, val email: String?, val photoUrl : Uri?)

    fun getUserModel(user : FirebaseUser) : UserModel{
            val uid = user.uid
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl
        return UserModel(uid, name, email, photoUrl)
    }

}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}


fun Intent.putParcelableExtra(key: String, value: Parcelable) {
    putExtra(key, value)
}



