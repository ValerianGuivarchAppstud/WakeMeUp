package com.vguivarc.wakemeup.transport.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.vguivarc.wakemeup.BuildConfig
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.base.BaseActivity
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.fragment_account.toolbar

class AboutFragment : Fragment(R.layout.fragment_about) {


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as BaseActivity).setSupportActionBar(toolbar)
        (activity as BaseActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        legals.setOnClickListener { goToLegals() }
        displayVersion()
    }

    private fun goToLegals() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.legals_link)))
        startActivity(intent)
    }

    private fun displayVersion() {
        version.text =
            getString(R.string.version, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)
    }
}
