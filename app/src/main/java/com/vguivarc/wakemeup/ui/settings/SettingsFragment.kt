package com.vguivarc.wakemeup.ui.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vguivarc.wakemeup.BuildConfig
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.base.BaseFragment
import com.vguivarc.wakemeup.ui.login.AuthActivity
import com.vguivarc.wakemeup.ui.login.LOGIN_REQUEST_CODE
import com.vguivarc.wakemeup.util.contactUs
import com.vguivarc.wakemeup.viewmodel.AccountViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import org.jetbrains.anko.support.v4.intentFor
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SettingsFragment : BaseFragment( R.layout.fragment_settings) {

    private val accountViewModel: AccountViewModel by sharedViewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // load user profile if exists
        accountViewModel.getAndUpdateUserInfo()
        configureList()
    }

    private fun configureList() {
        contentView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        context?.let { ctx ->
            val itemDecoration = androidx.recyclerview.widget.DividerItemDecoration(
                ctx,
                androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
            )
            ContextCompat.getDrawable(ctx, R.drawable.recycler_line_divider_with_padding)
                ?.let { drawable -> itemDecoration.setDrawable(drawable) }
            contentView.addItemDecoration(itemDecoration)
        }

        val filter = if (accountViewModel.isUserConnected()) {
            SettingsRubric.LOGIN
        } else {
            SettingsRubric.ACCOUNT
        }
        val list = SettingsRubric.values().filterNot { it == filter }
        contentView.adapter = SettingsAdapter(list) { settingsRubric ->
            onSettingsItemClick(settingsRubric)
        }
    }

    private fun onSettingsItemClick(settingsRubric: SettingsRubric) {
        when (settingsRubric) {
            SettingsRubric.LOGIN -> goToLogin()
            SettingsRubric.ACCOUNT -> {
                findNavController()
                    .navigate(R.id.action_settings_account)
            }
            SettingsRubric.ABOUT -> findNavController()
                .navigate(R.id.action_settings_about)
            SettingsRubric.CONTACT -> context?.apply {
                contactUs()
            }
            SettingsRubric.RATE -> {
                goToPlayStore()
            }
        }
    }

    private fun goToPlayStore() {
        val uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
                )
            )
        }
    }

    private fun goToLogin() {
        startActivityForResult(
            intentFor<AuthActivity>(),
            LOGIN_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_REQUEST_CODE) {
            configureList()
        }
    }
}
