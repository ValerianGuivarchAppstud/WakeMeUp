package com.vguivarc.wakemeup.transport.settings

import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.domain.external.AuthInteractor
import com.vguivarc.wakemeup.transport.alarm.AlarmListSideEffect
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

class SettingsViewModel(private val authInteractor: AuthInteractor) :
    ContainerHost<SettingsState, SettingsSideEffect>, ViewModel() {

    override val container =
        container<SettingsState, SettingsSideEffect>(
            SettingsState(),
            onCreate = ::onCreate
        )

    private fun onCreate(initialState: SettingsState) {
        getSettings()
    }

    fun getSettings() = intent {
        try {
            val isConnected =authInteractor.isUserConnected()
            reduce {
                state.copy(connected = isConnected)
            }

        } catch (exception: Exception) {
            Timber.e(exception)

            reduce {
                state.copy(connected = false)
            }

//            postSideEffect(SettingsSideEffect.Toast(R.string.general_error))
        }
    }

    fun navigationGoToLogin() = intent {
        postSideEffect(SettingsSideEffect.NavigationAction("SettingsLogin"))
    }

    fun navigationGoToAccount() = intent {
        postSideEffect(SettingsSideEffect.NavigationAction("SettingsAccount"))
    }

    fun actionLogout() = intent {
        authInteractor.logout()
        reduce {
                state.copy(connected = false)
            }
    }

    fun ok() = intent {
        postSideEffect(SettingsSideEffect.Ok)
    }
}
