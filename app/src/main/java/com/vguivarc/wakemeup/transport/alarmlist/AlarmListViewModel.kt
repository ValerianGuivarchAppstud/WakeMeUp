package com.vguivarc.wakemeup.transport.alarmlist

import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.AlarmInteractor
import com.vguivarc.wakemeup.domain.external.entity.Alarm
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber


class AlarmListViewModel(private val alarmInteractor: AlarmInteractor) :
    ContainerHost<AlarmListState, AlarmListSideEffect>, ViewModel() {

    override val container =
        container<AlarmListState, AlarmListSideEffect>(
            AlarmListState(),
            onCreate = ::onCreate
        )

    private fun onCreate(initialState: AlarmListState) {
        getAlarmList()
    }

    private fun getAlarmList() = intent {
        try {
            val list = alarmInteractor.getAlarms()

            reduce {
                Timber.e("state2")
                state.copy(alarmList = list.toList())
            }

        } catch (exception: Exception) {
            Timber.e(exception)

            reduce {
                state.copy(alarmList = emptyList(), currentEditingAlarm = null)
            }
            postSideEffect(AlarmListSideEffect.Toast(R.string.general_error))
        }
    }

    fun actionAddAlarm() {
        alarmInteractor.add()
        getAlarmList()
    }

    fun actionRemoveAlarm(alarm: Alarm) {
        alarmInteractor.remove(alarm)
        getAlarmList()
    }

    fun actionSwitchAlarm(alarm: Alarm) {
        alarmInteractor.switchReveil(alarm)
        getAlarmList()
    }

    fun actionSaveAlarm(alarm: Alarm) {
        alarmInteractor.save(alarm)
        getAlarmList()
    }

    fun actionDaySelected(alarm: Alarm, day: Alarm.DaysWeek) {
        alarmInteractor.actionDaySelected(alarm, day)
        getAlarmList()
    }

    fun actionRepeatSelected(alarm: Alarm, checked: Boolean) {
        alarmInteractor.actionRepeatSelected(alarm, checked)
        getAlarmList()
    }

    fun actionEditAlarm(alarm: Alarm?) = intent {
        reduce {
            if (state.currentEditingAlarm == alarm) {
                state.copy(currentEditingAlarm = null)
            } else {
                state.copy(currentEditingAlarm = alarm)
            }
        }
    }

    fun actionChangeAlarmTime(alarm: Alarm) = intent {
        postSideEffect(AlarmListSideEffect.OpenTimeEditor(alarm))

    }
}
/*
class AlarmListViewModel(private val alarmService: IAlarmProvider) : BaseViewModel() {

    private var _alarmsList = MediatorLiveData<List<Alarm>>()

    val alarmsList: LiveData<List<Alarm>> = _alarmsList

    init {
        _alarmsList.addSource(alarmService.getAlarms()) { alarms ->
            _alarmsList.value = alarms
        }
    }

    fun save(alarm: Alarm) {
        alarmService.save(alarm)
    }


    fun snoozeAlarm(idAlarm: Int) {
        alarmService.snoozeAlarm(idAlarm)
    }

    fun stopAlarm(idAlarm: Int) {
        alarmService.stopAlarm(idAlarm)
    }

    fun buttonAddAlarm() {
        TODO("Not yet implemented")
    }
}
*/