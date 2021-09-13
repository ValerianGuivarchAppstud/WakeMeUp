package com.vguivarc.wakemeup.transport.alarmlist

import android.app.*
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.databinding.FragmentAlarmListBinding
import com.vguivarc.wakemeup.domain.external.entity.Alarm
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.orbitmvi.orbit.viewmodel.observe
import timber.log.Timber
import java.util.*


class AlarmListFragment : Fragment(R.layout.fragment_alarm_list) , AlarmListAdapter.AlarmsListAdapterListener {

    private val viewModel by viewModel<AlarmListViewModel>()

    private var _binding: FragmentAlarmListBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlarmListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.boutonAddReveil.setOnClickListener {
            viewModel.actionAddAlarm()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observe(this, state = ::render, sideEffect = ::handleSideEffect)
    }

    private fun render(state: AlarmListState) {
        Timber.d("render $state")
        val list = view?.findViewById<RecyclerView>(R.id.list_alarms) ?: return
        if (list.adapter == null) {
            list.adapter = AlarmListAdapter(state.alarmList, this)
            list.layoutManager = LinearLayoutManager(context)
        } else {
            (list.adapter as? AlarmListAdapter)?.updateData(state.alarmList, state.currentEditingAlarm)
        }
    }

    private fun handleSideEffect(sideEffect: AlarmListSideEffect) {
        when (sideEffect) {
            is AlarmListSideEffect.Toast -> Toast.makeText(
                context,
                sideEffect.textResource,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    //TODO ajouter le "supprimé / annuler"?


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
                viewModel.actionSaveAlarm(alarm)
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
        viewModel.actionSwitchAlarm(alarm)
    }

    override fun onAlarmDelete(alarm: Alarm) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Supprimer réveil")
        builder.setMessage("Voulez-vous supprimer ce réveil ?")
        builder.setPositiveButton("Supprimer") { _, _ ->
            viewModel.actionRemoveAlarm(alarm)
        }
        builder.setNeutralButton("Annuler") { _, _ -> }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onAlarmRepeatCheck(alarm: Alarm, isChecked: Boolean) {
        viewModel.actionRepeatSelected(alarm, isChecked)
        // TODO name : wakemeupb4
    }

    override fun onAlarmDaySelected(alarm: Alarm, day: Alarm.DaysWeek) {
        viewModel.actionDaySelected(alarm, day)
    }

    override fun onAlarmEditSelected(alarm: Alarm?) {
        viewModel.actionEditAlarm(alarm)
    }

}

/*
class AlarmListFragment : BaseFragment(R.layout.fragment_alarm_list), AlarmListAdapter.AlarmsListAdapterListener {

    private val viewModel: AlarmListViewModel by inject()

    private lateinit var listAdapter: AlarmListAdapter

    private val alarms = mutableListOf<Alarm>()

    private lateinit var recyclerView: RecyclerView

    private lateinit var boutonAddReveil : FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.list_alarms)

        listAdapter = AlarmListAdapter(alarms, this)
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

    // TODO notif n'est pas le bon truc à faire pour ça
    private fun notification(textProchainReveil: String) {

        lateinit var notificationChannel: NotificationChannel
        lateinit var builder: Notification
        val channelId = "com.vguivarc.wakemeup.util.notification"
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
*/