package com.vguivarc.wakemeup.transport

import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.AlarmInteractor
import com.vguivarc.wakemeup.domain.external.AuthInteractor
import com.vguivarc.wakemeup.domain.external.RingingInteractor
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

class MainActivityViewModel(private val authInteractor: AuthInteractor,
                            private val ringingInteractor: RingingInteractor) :
    ContainerHost<MainActivityState, MainActivitySideEffect>, ViewModel() {

    override val container =
        container<MainActivityState, MainActivitySideEffect>(
            MainActivityState(),
            onCreate = ::onCreate
        )

    private fun onCreate(initialState: MainActivityState) {
        getCurrentUser()
        getRinging()
    }

    private fun getCurrentUser() = intent {
        try {
            val isConnected =authInteractor.isUserConnected()
            reduce {
                state.copy(isConnected = isConnected)
            }

        } catch (exception: Exception) {
            Timber.e(exception)

            reduce {
                state.copy(isConnected = false)
            }

            postSideEffect(MainActivitySideEffect.Toast(R.string.general_error))
        }
    }

    private fun getRinging() = intent {
        try {
            val waitingRingingList =ringingInteractor.getWaitingRingingList()
            val newRinging = waitingRingingList.any { it.seen }
            reduce {
                state.copy(nbNewRinging = waitingRingingList.size, newRinging = newRinging)
            }

        } catch (exception: Exception) {
            Timber.e(exception)

            reduce {
                state.copy(nbNewRinging = 0)
            }

            postSideEffect(MainActivitySideEffect.Toast(R.string.general_error))
        }
    }


    private fun getNotifications() = intent {
        // TODO
    }
}
