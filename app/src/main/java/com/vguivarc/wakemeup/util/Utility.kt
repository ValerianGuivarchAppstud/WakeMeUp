package com.vguivarc.wakemeup.util

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import com.vguivarc.wakemeup.AppWakeUp
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


object Utility {

    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            // Log exception
            null
        }
    }


    fun convertDuration(duration: Int): String {

        val minutes = duration / 60
        val seconds = duration % 60

        return String.format("%d:%02d", minutes, seconds)
    }

    fun createSimpleToast(text: String){
        Toast.makeText(
            AppWakeUp.appContext,
            text,
            Toast.LENGTH_SHORT
        ).show()
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



