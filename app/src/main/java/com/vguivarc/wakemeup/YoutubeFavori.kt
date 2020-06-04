package com.vguivarc.wakemeup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

class YoutubeFavori : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_favori)



        val extras = intent.extras

        val intent = Intent(this, MainActivity::class.java)
        if (extras != null) {
            val value1 = extras.getString(Intent.EXTRA_TEXT)
            if (value1 != null) {
                Timber.e(value1)
                val link = value1.substring(value1.indexOf("youtu.be/") + 9)
                Timber.e(link)
                intent.putExtra("favyt", link)
            }
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)

    }
}
