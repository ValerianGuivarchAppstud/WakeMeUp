package com.vguivarc.wakemeup.ui.alarm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.entity.Alarm

class AlarmsListAdapter(
    private val alarmsList: List<Alarm>,
    private val listener: AlarmsListAdapterListener?
) : RecyclerView.Adapter<AlarmsListAdapter.ViewHolder>(),
    View.OnClickListener {

    interface AlarmsListAdapterListener {
        fun onAlarmClicked(alarm: Alarm, itemView: View)
        fun onAlarmSwitched(alarm: Alarm)
        fun onAlarmDelete(alarm: Alarm)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val alarmCardView = itemView.findViewById<CardView>(R.id.alarm_card_view)!!
        var itemAlarmHeure = itemView.findViewById<TextView>(R.id.item_alarm_heure)!!
        var itemAlarmJours = itemView.findViewById<TextView>(R.id.item_alarm_jours)!!
        var itemAlarmSwitch = itemView.findViewById<SwitchCompat>(R.id.item_alarm_switch)!!
        var itemAlarmDelete = itemView.findViewById<ImageButton>(R.id.fav_tv_delete)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_alarm, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alarm = alarmsList.toList()[position]
        with(holder) {
            alarmCardView.tag = alarm
            alarmCardView.setOnClickListener(this@AlarmsListAdapter)
            itemAlarmHeure.text = alarm.getHeureTexte()
            itemAlarmJours.text = alarm.getJoursTexte()
            itemAlarmSwitch.isChecked = alarm.isActif
            itemAlarmSwitch.setOnClickListener {
                listener?.onAlarmSwitched(alarm)
            }
            itemAlarmDelete.setOnClickListener {
                listener?.onAlarmDelete(alarm)
            }
        }
    }

    override fun getItemCount(): Int = alarmsList.size

    override fun onClick(v: View) {
        listener?.onAlarmClicked(v.tag as Alarm, v)
    }
}