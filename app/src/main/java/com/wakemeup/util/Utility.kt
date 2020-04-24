package com.wakemeup.util

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast


object Utility {

    fun convertDuration(duration: Int): String {

        val minutes = duration / 60
        val seconds = duration % 60

        return String.format("%d:%02d", minutes, seconds)
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

fun creatToast(activity : Activity, text : String){
    Toast.makeText(
        activity!!.application,
        text,
        Toast.LENGTH_SHORT
    ).show()
}

