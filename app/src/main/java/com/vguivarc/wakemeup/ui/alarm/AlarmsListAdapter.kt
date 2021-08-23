package com.vguivarc.wakemeup.ui.alarm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.entity.Alarm
import org.jetbrains.anko.textColor

class AlarmsListAdapter(
    private val alarmsList: List<Alarm>,
    private val listener: AlarmsListAdapterListener?
) : RecyclerView.Adapter<AlarmsListAdapter.ViewHolder>() {

    private var currentEditingAlarm: ViewHolder? = null

    interface AlarmsListAdapterListener {
        fun onAlarmTimeClicked(alarm: Alarm)
        fun onAlarmSwitched(alarm: Alarm)
        fun onAlarmDelete(alarm: Alarm)
        fun onAlarmRepeatCheck(alarm: Alarm, isChecked: Boolean)
        fun onAlarmDaySelected(alarm: Alarm, day: Alarm.DaysWeek)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemAlarmDaysList = itemView.findViewById<ConstraintLayout>(R.id.item_alarm_days_list)
        val itemAlarmMonday = itemView.findViewById<RelativeLayout>(R.id.alarm_day_button_monday)
        val itemAlarmMondayBtn = itemView.findViewById<Button>(R.id.alarm_day_button_monday_btn)
        val itemAlarmTuesday = itemView.findViewById<RelativeLayout>(R.id.alarm_day_button_tuesday)
        val itemAlarmTuesdayBtn = itemView.findViewById<Button>(R.id.alarm_day_button_tuesday_btn)
        val itemAlarmWednesday = itemView.findViewById<RelativeLayout>(R.id.alarm_day_button_wednesday)
        val itemAlarmWednesdayBtn = itemView.findViewById<Button>(R.id.alarm_day_button_wednesday_btn)
        val itemAlarmThursday = itemView.findViewById<RelativeLayout>(R.id.alarm_day_button_thursday)
        val itemAlarmThursdayBtn = itemView.findViewById<Button>(R.id.alarm_day_button_thursday_btn)
        val itemAlarmFriday = itemView.findViewById<RelativeLayout>(R.id.alarm_day_button_friday)
        val itemAlarmFridayBtn = itemView.findViewById<Button>(R.id.alarm_day_button_friday_btn)
        val itemAlarmSaturday = itemView.findViewById<RelativeLayout>(R.id.alarm_day_button_saturday)
        val itemAlarmSaturdayBtn = itemView.findViewById<Button>(R.id.alarm_day_button_saturday_btn)
        val itemAlarmSunday = itemView.findViewById<RelativeLayout>(R.id.alarm_day_button_sunday)
        val itemAlarmSundayBtn = itemView.findViewById<Button>(R.id.alarm_day_button_sunday_btn)
        var itemAlarmTime = itemView.findViewById<TextView>(R.id.item_alarm_time)!!
        var itemAlarmRepeatCheck = itemView.findViewById<CheckBox>(R.id.edit_alarm_repeat_check)!!
        var itemAlarmDays = itemView.findViewById<TextView>(R.id.item_alarm_days)!!
        var itemAlarmSwitch = itemView.findViewById<SwitchCompat>(R.id.item_alarm_switch)!!
        var itemAlarmDelete = itemView.findViewById<TextView>(R.id.fav_tv_delete)!!
        var itemAlarmEditArrow = itemView.findViewById<ImageView>(R.id.id_edit_alarm_arrow)!!
        var itemAlarmEditLayout = itemView.findViewById<ConstraintLayout>(R.id.edit_alarm_layout)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_alarm, parent, false)
        return ViewHolder(viewItem)
    }

    fun closeAlarmEdit() {
        closeEditingAlarm(currentEditingAlarm)
        currentEditingAlarm = null
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

/*            itemAlarmTuesday.setOnClickListener {
                listener?.onAlarmDaySelected(alarm, Alarm.DaysWeek.Tuesday)
            }
            itemAlarmWednesday.setOnClickListener {
                listener?.onAlarmDaySelected(alarm, Alarm.DaysWeek.Wednesday)
            }
            itemAlarmThursday.setOnClickListener {
                listener?.onAlarmDaySelected(alarm, Alarm.DaysWeek.Thursday)
            }
            itemAlarmFriday.setOnClickListener {
                listener?.onAlarmDaySelected(alarm, Alarm.DaysWeek.Friday)
            }
            itemAlarmSaturday.setOnClickListener {
                listener?.onAlarmDaySelected(alarm, Alarm.DaysWeek.Saturday)
            }
            itemAlarmSunday.setOnClickListener {
                listener?.onAlarmDaySelected(alarm, Alarm.DaysWeek.Sunday)
            }*/
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
