package com.vguivarc.wakemeup.ui.alarm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
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
        val itemAlarmMonday = itemView.findViewById<Button>(R.id.alarm_day_button_monday)!!
        val itemAlarmTuesday = itemView.findViewById<Button>(R.id.alarm_day_button_tuesday)!!
        val itemAlarmWednesday = itemView.findViewById<Button>(R.id.alarm_day_button_wednesday)!!
        val itemAlarmThursday = itemView.findViewById<Button>(R.id.alarm_day_button_thursday)!!
        val itemAlarmFriday = itemView.findViewById<Button>(R.id.alarm_day_button_friday)!!
        val itemAlarmSaturday = itemView.findViewById<Button>(R.id.alarm_day_button_saturday)!!
        val itemAlarmSunday = itemView.findViewById<Button>(R.id.alarm_day_button_sunday)!!
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
            dealWithDaysButton(alarm, itemAlarmMonday, Alarm.DaysWeek.Monday)

            itemAlarmTuesday.setOnClickListener {
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
            }
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

    private fun dealWithDaysButton(alarm: Alarm, itemAlarmDay: Button, day: Alarm.DaysWeek) {
        itemAlarmDay.setOnClickListener {
            listener?.onAlarmDaySelected(alarm, day)
        }
        if (alarm.listActifDays.contains(day)) {
            itemAlarmDay.textColor = R.color.texte_bouton
            //          itemAlarmDay.background = itemAlarmDay.resources.getColor(R.color.transparent, null)
        } else {
            itemAlarmDay.textColor = R.color.colorPrimary
//            itemAlarmDay.background = itemAlarmDay.resources.getColor(R.color.transparent, null)
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
