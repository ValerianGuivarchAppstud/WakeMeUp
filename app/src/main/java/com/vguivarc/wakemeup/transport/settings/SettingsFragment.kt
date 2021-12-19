package com.vguivarc.wakemeup.transport.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vguivarc.wakemeup.BuildConfig
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.databinding.FragmentSettingsBinding
import com.vguivarc.wakemeup.transport.old.auth.AuthActivity
import com.vguivarc.wakemeup.util.contactUs
import kotlinx.android.synthetic.main.fragment_settings.*
import org.jetbrains.anko.support.v4.intentFor
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.orbitmvi.orbit.viewmodel.observe
import timber.log.Timber


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val viewModel by viewModel<SettingsViewModel>()


    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observe(this, state = ::render, sideEffect = ::handleSideEffect)
    }

    private fun render(state: SettingsState) {
        Timber.d("render $state")
        configureList(state.connected)
    }

    private fun handleSideEffect(sideEffect: SettingsSideEffect) {
        when (sideEffect) {
            is SettingsSideEffect.Toast -> Toast.makeText(
                context,
                sideEffect.textResource,
                Toast.LENGTH_SHORT
            ).show()
        }
    }




    private fun configureList(connected: Boolean) {
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

        val filter = if (connected) {
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
        startActivity(
            intentFor<AuthActivity>()
        )
    }

 /*   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

            viewModel.getSettings()

    }*/

    override fun onResume() {
        super.onResume()
        viewModel.getSettings()
    }
}
