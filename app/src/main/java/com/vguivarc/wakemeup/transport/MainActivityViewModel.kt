package com.vguivarc.wakemeup.transport

import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.AuthInteractor
import com.vguivarc.wakemeup.domain.external.RingingInteractor
import com.vguivarc.wakemeup.transport.alarm.AlarmListSideEffect
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
        getRingingToolbarData()
        getNotifications()
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

    private fun getRingingToolbarData() = intent {
        try {
            if(authInteractor.isUserConnected()) {
                val waitingRingingList =ringingInteractor.getWaitingRingingList()
                val newRinging = waitingRingingList.any { it.seen }
                reduce {
                    state.copy(nbNewRinging = waitingRingingList.size, newRinging = newRinging, isConnected = true)
                }
            } else {
                reduce {
                    state.copy(nbNewRinging = 0, newRinging = false, isConnected = false)
                }
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
        try {
            if(authInteractor.isUserConnected()) {
                // TODO
            } else {
                reduce {
                    state.copy(nbNewNotification = 0, newNotification = false, isConnected = false)
                }
            }
        } catch (exception: Exception) {
            Timber.e(exception)

            reduce {
                state.copy(nbNewRinging = 0)
            }

            postSideEffect(MainActivitySideEffect.Toast(R.string.general_error))
        }
    }

    fun navigate(route: String, top: Boolean = false) = intent {
        postSideEffect(MainActivitySideEffect.Navigate(route, top))

    }



    fun ok() = intent {
        postSideEffect(MainActivitySideEffect.Ok)
    }
}
