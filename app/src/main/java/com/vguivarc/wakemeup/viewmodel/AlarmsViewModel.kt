package com.vguivarc.wakemeup.viewmodel

import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.domain.external.AlarmInteractor
import com.vguivarc.wakemeup.transport.ringingalarm.RingingAlarmSideEffect
import com.vguivarc.wakemeup.transport.ringingalarm.RingingAlarmState
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber


class RingingAlarmViewModel(private val alarmInteractor: AlarmInteractor) :
    ContainerHost<RingingAlarmState, RingingAlarmSideEffect>, ViewModel() {

    override val container =
        container<RingingAlarmState, RingingAlarmSideEffect>(
            RingingAlarmState(),
            onCreate = ::onCreate
        )

    private fun onCreate(initialState: RingingAlarmState) {
        getRingingAlarm()
    }

    fun getRingingAlarm() = intent {

        try {
            val ringingAlarm = alarmInteractor.getAlarms().get(0)//TODO fix

            reduce {
                state.copy(ringingAlarm = ringingAlarm)
            }

        } catch (exception: Exception) {
            Timber.e(exception)

            reduce {
                state.copy(ringingAlarm = null)
            }

            // postSideEffect(RingingAlarmSideEffect.Toast(R.string.general_error))
        }
    }

    fun stopAlarm(idReveil: Int) {

    }

    fun snoozeAlarm(idReveil: Int) {

    }
}