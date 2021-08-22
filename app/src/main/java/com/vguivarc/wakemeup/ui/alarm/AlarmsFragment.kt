package com.vguivarc.wakemeup.ui.alarm

import android.app.*
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.base.BaseFragment
import com.vguivarc.wakemeup.domain.entity.Alarm
import com.vguivarc.wakemeup.ui.MainActivity
import com.vguivarc.wakemeup.viewmodel.AlarmsViewModel
import org.koin.android.ext.android.inject
import java.util.*

class AlarmsFragment : BaseFragment(R.layout.fragment_alarms_list), AlarmsListAdapter.AlarmsListAdapterListener {

    private val viewModel: AlarmsViewModel by inject()

    private lateinit var listAdapter: AlarmsListAdapter

    private val alarms = mutableListOf<Alarm>()

    private lateinit var recyclerView: RecyclerView

    private lateinit var boutonAddReveil : FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.list_alarms)

        listAdapter = AlarmsListAdapter(alarms, this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = listAdapter
        listAdapter.notifyDataSetChanged()

        viewModel.alarmsList.observe(
            viewLifecycleOwner,
            { newAlarmsList ->
                updateAlarms(
                    newAlarmsList
                )
            }
        )

        boutonAddReveil = view.findViewById(R.id.bouton_add_reveil)
        boutonAddReveil.setOnClickListener {
            viewModel.save(Alarm())
        }

    }

    private fun updateAlarms(newAlarmsList: List<Alarm>) {
        alarms.clear()
        alarms.addAll(newAlarmsList)
        listAdapter.notifyDataSetChanged()
        if (alarms.isNotEmpty()) {
            var minR = newAlarmsList[0]
            for (r in newAlarmsList) {
                if (r.nextAlarmCalendar.before(minR.nextAlarmCalendar)) {
                    minR = r
                }
            }
            //  notification(""+minR.getText())
        } else {
            deleteNotification()
        }
    }

    override fun onAlarmTimeClicked(alarm: Alarm) {
        if (alarm.hour == -1) {
            // Get Current Time
            val c: Calendar = Calendar.getInstance()
            alarm.hour = c.get(Calendar.HOUR_OF_DAY)
            alarm.minute = c.get(Calendar.MINUTE)
        }
        // Time Set Listener.
        val timeSetListener =
            OnTimeSetListener { view, hourOfDay, minute ->
                alarm.hour = hourOfDay
                alarm.minute = minute
                viewModel.save(alarm)
            }

        // Create TimePickerDialog:
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            timeSetListener, alarm.hour, alarm.minute, true
        )

        // Show
        timePickerDialog.show()
    }

    override fun onAlarmSwitched(alarm: Alarm) {
        viewModel.switchAlarm(alarm)
    }

    override fun onAlarmDelete(alarm: Alarm) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Supprimer réveil")
        builder.setMessage("Voulez-vous supprimer ce réveil ?")
        builder.setPositiveButton("Supprimer") { _, _ ->
            viewModel.remove(alarm)
            listAdapter.closeAlarmEdit()
        }
        builder.setNeutralButton("Annuler") { _, _ -> }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onAlarmRepeatCheck(alarm: Alarm, isChecked: Boolean) {
        alarm.isRepeated = isChecked
        viewModel.save(alarm)
    }

    override fun onAlarmDaySelected(alarm: Alarm, day: Alarm.DaysWeek) {
        if (alarm.listActifDays.contains(day)) {
            alarm.listActifDays.remove(day)
        } else {
            alarm.listActifDays.add(day)
        }
        viewModel.save(alarm)
    }

    // TODO notif n'est pas le bon truc à faire pour ça
    private fun notification(textProchainReveil: String) {

        lateinit var notificationChannel: NotificationChannel
        lateinit var builder: Notification
        val channelId = "com.vguivarc.wakemeup.notification"
        val description = "Music Me ! Alarme prévue ...(TODO add texte)"
        val notificationManage = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val mainActivityIntent = Intent(requireActivity(), MainActivity::class.java)
        val pi = PendingIntent.getActivity(requireContext(), 0, mainActivityIntent, 0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_LOW)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.enableVibration(true)
            notificationManage.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(requireContext(), channelId)
                .setContentTitle("Prochain réveil prévu :")
                .setSmallIcon(R.drawable.main_logo)
                .setContentText(textProchainReveil)
                .setContentIntent(pi)
                .build()
        } else {
            @Suppress("DEPRECATION")
            builder = Notification.Builder(requireContext())
                .setContentTitle("Votre réveil social sonne!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Clique sur moi!")
                .setContentIntent(pi)
                .build()
        }
        notificationManage.notify(0, builder)
    }

    private fun deleteNotification() {
        val notificationManage = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManage.cancelAll()
    }
}
