package com.vguivarc.wakemeup.transport.alarmlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.entity.Alarm
import org.jetbrains.anko.textColor

class AlarmListAdapter(
    private var alarmsList: List<Alarm>,
    private val listener: AlarmsListAdapterListener?
) : RecyclerView.Adapter<AlarmListAdapter.ViewHolder>() {


    @SuppressLint("NotifyDataSetChanged")
    fun updateData(alarmsList: List<Alarm>) {
        this.alarmsList = alarmsList
        notifyDataSetChanged()
    }

    private var currentEditingAlarm: ViewHolder? = null

    interface AlarmsListAdapterListener {
        fun onAlarmTimeClicked(alarm: Alarm)
        fun onAlarmSwitched(alarm: Alarm)
        fun onAlarmDelete(alarm: Alarm)
        fun onAlarmRepeatCheck(alarm: Alarm, isChecked: Boolean)
        fun onAlarmDaySelected(alarm: Alarm, day: Alarm.DaysWeek)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemAlarmDaysList: ConstraintLayout = itemView.findViewById(R.id.item_alarm_days_list)
        val itemAlarmMonday: RelativeLayout = itemView.findViewById(R.id.alarm_day_button_monday)
        val itemAlarmMondayBtn: Button = itemView.findViewById(R.id.alarm_day_button_monday_btn)
        val itemAlarmTuesday: RelativeLayout = itemView.findViewById(R.id.alarm_day_button_tuesday)
        val itemAlarmTuesdayBtn: Button = itemView.findViewById(R.id.alarm_day_button_tuesday_btn)
        val itemAlarmWednesday: RelativeLayout = itemView.findViewById(R.id.alarm_day_button_wednesday)
        val itemAlarmWednesdayBtn: Button = itemView.findViewById(R.id.alarm_day_button_wednesday_btn)
        val itemAlarmThursday: RelativeLayout = itemView.findViewById(R.id.alarm_day_button_thursday)
        val itemAlarmThursdayBtn: Button = itemView.findViewById(R.id.alarm_day_button_thursday_btn)
        val itemAlarmFriday: RelativeLayout = itemView.findViewById(R.id.alarm_day_button_friday)
        val itemAlarmFridayBtn: Button = itemView.findViewById(R.id.alarm_day_button_friday_btn)
        val itemAlarmSaturday: RelativeLayout = itemView.findViewById(R.id.alarm_day_button_saturday)
        val itemAlarmSaturdayBtn: Button = itemView.findViewById(R.id.alarm_day_button_saturday_btn)
        val itemAlarmSunday: RelativeLayout = itemView.findViewById(R.id.alarm_day_button_sunday)
        val itemAlarmSundayBtn: Button = itemView.findViewById(R.id.alarm_day_button_sunday_btn)
        var itemAlarmTime : TextView= itemView.findViewById(R.id.item_alarm_time)!!
        var itemAlarmRepeatCheck : CheckBox= itemView.findViewById(R.id.edit_alarm_repeat_check)!!
        var itemAlarmDays : TextView= itemView.findViewById(R.id.item_alarm_days)!!
        var itemAlarmSwitch : SwitchCompat= itemView.findViewById(R.id.item_alarm_switch)!!
        var itemAlarmDelete : TextView= itemView.findViewById(R.id.fav_tv_delete)!!
        var itemAlarmEditArrow : ImageView= itemView.findViewById(R.id.id_edit_alarm_arrow)!!
        var itemAlarmEditLayout : ConstraintLayout = itemView.findViewById(R.id.edit_alarm_layout)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_alarm, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alarm = alarmsList.toList()[position]
        with(holder) {
            itemAlarmEditArrow.setOnClickListener {
                closeEditingAlarm(currentEditingAlarm)
                if (currentEditingAlarm != holder) {
                    currentEditingAlarm = holder
                    openEditingAlarm(holder)
                } else {
                    currentEditingAlarm = null
                }
            }
            itemAlarmTime.text = alarm.getHeureTexte()
            itemAlarmDays.text = alarm.getJoursTexte()
            itemAlarmTime.setOnClickListener {
                listener?.onAlarmTimeClicked(alarm)
            }
            itemAlarmSwitch.isChecked = alarm.isActif
            itemAlarmSwitch.setOnClickListener {
                listener?.onAlarmSwitched(alarm)
            }
            itemAlarmDelete.setOnClickListener {
                listener?.onAlarmDelete(alarm)
            }
            dealWithDaysButton(alarm, itemAlarmMonday, itemAlarmMondayBtn, Alarm.DaysWeek.Monday)
            dealWithDaysButton(alarm, itemAlarmTuesday, itemAlarmTuesdayBtn, Alarm.DaysWeek.Tuesday)
            dealWithDaysButton(alarm, itemAlarmWednesday, itemAlarmWednesdayBtn, Alarm.DaysWeek.Wednesday)
            dealWithDaysButton(alarm, itemAlarmThursday, itemAlarmThursdayBtn, Alarm.DaysWeek.Thursday)
            dealWithDaysButton(alarm, itemAlarmFriday, itemAlarmFridayBtn, Alarm.DaysWeek.Friday)
            dealWithDaysButton(alarm, itemAlarmSaturday, itemAlarmSaturdayBtn, Alarm.DaysWeek.Saturday)
            dealWithDaysButton(alarm, itemAlarmSunday, itemAlarmSundayBtn, Alarm.DaysWeek.Sunday)

            itemAlarmRepeatCheck.setOnCheckedChangeListener { _, isChecked ->
                listener?.onAlarmRepeatCheck(alarm, isChecked)
                if (isChecked) {
                    itemAlarmDaysList.visibility = View.VISIBLE
                } else {
                    itemAlarmDaysList.visibility = View.GONE
                }
            }
        }
    }

    private fun dealWithDaysButton(alarm: Alarm, itemAlarmDay: RelativeLayout, itemAlarmDayBtn: Button, day: Alarm.DaysWeek) {
        itemAlarmDay.setOnClickListener {
            listener?.onAlarmDaySelected(alarm, day)
        }
        itemAlarmDayBtn.setOnClickListener {
            listener?.onAlarmDaySelected(alarm, day)
        }
        if (alarm.listActifDays.contains(day)) {
            itemAlarmDayBtn.textColor = R.color.white_transparent_70
            itemAlarmDayBtn.background = ContextCompat.getDrawable(itemAlarmDay.context, R.drawable.rounded_button)
        } else {
            itemAlarmDayBtn.textColor = R.color.colorPrimary
            itemAlarmDayBtn.background = null
        }
    }

    private fun closeEditingAlarm(currentEditingAlarm: ViewHolder?) {
        currentEditingAlarm?.itemAlarmEditArrow?.rotation = 0f
        currentEditingAlarm?.itemAlarmEditLayout?.visibility = View.GONE
        currentEditingAlarm?.itemAlarmDelete?.visibility = View.GONE
        currentEditingAlarm?.itemAlarmDays?.visibility = View.VISIBLE
    }

    private fun openEditingAlarm(currentEditingAlarm: ViewHolder) {
        currentEditingAlarm.itemAlarmEditArrow.rotation = 180f
        currentEditingAlarm.itemAlarmEditLayout.visibility = View.VISIBLE
        currentEditingAlarm.itemAlarmDelete.visibility = View.VISIBLE
        currentEditingAlarm.itemAlarmDays.visibility = View.GONE
    }

    override fun getItemCount(): Int = alarmsList.size
}
