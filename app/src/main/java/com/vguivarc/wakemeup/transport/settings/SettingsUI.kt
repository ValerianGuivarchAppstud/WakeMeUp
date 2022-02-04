package com.vguivarc.wakemeup.transport.settings

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vguivarc.wakemeup.transport.routeViewModel

// TODO refresh pas
@Composable
fun SettingsScreen(navController: NavController) {
    val settingsViewModel: SettingsViewModel = remember { navController.routeViewModel() }

    val state by settingsViewModel.container.stateFlow.collectAsState()

    val side by settingsViewModel.container.sideEffectFlow.collectAsState(initial = null)

    SettingsContent(settingsViewModel, state.connected)

    side?.let {
        handleSideEffect(settingsViewModel, LocalContext.current, navController, it)
    }
    if(navController.currentBackStackEntry?.savedStateHandle?.contains("back") == true) {
        settingsViewModel.getSettings()
    }
    settingsViewModel.ok()
}


@Composable
fun SettingsContent(
    settingsViewModel: SettingsViewModel?,
    connected: Boolean
) {
    Scaffold(
        content = {
            Column {
                if(!connected) {
                    SettingsItemContent(SETTINGS.LOGIN) {
                        settingsViewModel?.navigationGoToLogin()
                    }
                }
                if(connected) {
                    SettingsItemContent(SETTINGS.ACCOUNT) {
                        settingsViewModel?.navigationGoToAccount()
                    }
                }
                if(connected) {
                    SettingsItemContent(SETTINGS.LOGOUT) {
                        settingsViewModel?.actionLogout()
                    }
                }
            }
        }
    )
}



@Composable
fun SettingsItemContent(
    setting: SETTINGS,
    onClick: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(all = 8.dp)
        .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = setting.text,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )
        Icon(Icons.Filled.ArrowForward, "Arrow open")
        }

    }

private fun handleSideEffect(
    settingsViewModel: SettingsViewModel,
    context: Context, navController: NavController, sideEffect: SettingsSideEffect
) {
    when (sideEffect) {
        is SettingsSideEffect.Toast -> Toast.makeText(
            context,
            sideEffect.textResource,
            Toast.LENGTH_SHORT
        ).show()
        is SettingsSideEffect.NavigationAction -> navController.navigate(sideEffect.route)
    }
}

@Preview
@Composable
fun SettingsContentPreview() {
    SettingsContent(null, true)
}


/*
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
            is SettingsSideEffect.NavigationAction -> navi
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

    override fun onResume() {
        super.onResume()
        viewModel.getSettings()
    }
}
*/