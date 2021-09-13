package com.vguivarc.wakemeup.transport.ringingalarm

import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.AlarmInteractor
import com.vguivarc.wakemeup.domain.external.RingingInteractor
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

class RingingAlarmViewModel(private val ringingInteractor: RingingInteractor,
                            private val alarmInteractor: AlarmInteractor) :
    ContainerHost<RingingAlarmState, RingingAlarmSideEffect>, ViewModel() {

    override val container =
        container<RingingAlarmState, RingingAlarmSideEffect>(
            RingingAlarmState(),
            onCreate = ::onCreate
        )

    private fun onCreate(initialState: RingingAlarmState) {
        getNextRinging()
    }


    fun getAlarm(idReveil: Int) = intent {
        reduce {
            val alarm = alarmInteractor.getAlarms().filter { it.idAlarm == idReveil }.firstOrNull()
            state.copy(alarm = alarm)
        }
    }

    fun getNextRinging() = intent {
        reduce {
            state.copy(step = RingingAlarmStep.WaitingForNextRinging)
        }

        try {
            val ringing = ringingInteractor.getNextRinging()
            if(ringing==null){
                reduce {
                    state.copy(step = RingingAlarmStep.NoNextRinging)
                }
            } else {
                reduce {
                    state.copy(ringing = ringing, step = RingingAlarmStep.WaitingForYoutubeReader)
                }
            }
        } catch (exception: Exception) {
            Timber.e(exception)

            reduce {
                state.copy(ringing = null, step = RingingAlarmStep.NoNextRinging)
            }

            postSideEffect(RingingAlarmSideEffect.Toast(R.string.general_error))
        }
    }


    fun stopAlarm()= intent {
        state.ringing?.let {
            ringingInteractor.stopAlarm(it)
        }
    }

    fun snoozeAlarm()= intent {
        state.ringing?.let {
            ringingInteractor.snoozeAlarm(it)
        }
    }

    fun errorPlayYoutubeSong() = intent {
        reduce {
            state.copy(ringing = null, step = RingingAlarmStep.NoNextRinging)
        }
    }

    fun youtubePlayerReady() =intent {
        reduce {
            state.copy(step = RingingAlarmStep.ReadyToPlay)
        }
    }

    fun isPlaying() =intent {
        reduce {
            state.copy(step = RingingAlarmStep.Playing)
        }
    }
}
