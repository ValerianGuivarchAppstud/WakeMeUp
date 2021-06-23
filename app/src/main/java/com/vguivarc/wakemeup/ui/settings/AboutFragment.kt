package com.vguivarc.wakemeup.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vguivarc.wakemeup.BuildConfig
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.base.BaseActivity
import com.vguivarc.wakemeup.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.fragment_account.toolbar


class AboutFragment : BaseFragment() {

    private var finger = 0
    private var maxTouch = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        finger = 0
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

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
