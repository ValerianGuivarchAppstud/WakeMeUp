package com.vguivarc.wakemeup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber


class LinkContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link)

        val link = intent.dataString
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("link", link!!.substring(link.indexOf("id=")+3))
        Timber.e(link!!.substring(link.indexOf("id=")+3))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)

    }


}
