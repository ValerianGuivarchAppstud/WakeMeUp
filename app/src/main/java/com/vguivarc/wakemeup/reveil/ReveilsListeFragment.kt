package com.vguivarc.wakemeup.reveil

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vguivarc.wakemeup.repo.ViewModelFactory
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.MainActivity
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.util.Utility
import com.vguivarc.wakemeup.util.putParcelableExtra
import kotlinx.android.synthetic.main.fragment_reveil_list.view.*

class ReveilsListeFragment : Fragment(), ReveilListeAdapter.ReveilListAdapterListener {


    private lateinit var viewModel: ReveilListeViewModel
    private lateinit var adapter: ReveilListeAdapter
    private val reveils = mutableMapOf<Int, ReveilModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(false)

        val factory = ViewModelFactory(AppWakeUp.repository)


        viewModel = ViewModelProvider(this, factory).get(ReveilListeViewModel::class.java)
        viewModel.getReveilsListeLiveData().observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { nouvelleListeReveils ->
                updateReveilsListe(
                    nouvelleListeReveils
                )
            })
    }


    private fun updateReveilsListe(nouvelleListeReveils: Map<Int, ReveilModel>) {
        reveils.clear()
        reveils.putAll(nouvelleListeReveils)
        adapter.notifyDataSetChanged()
        if(reveils.size>0){
            var minR = nouvelleListeReveils.toList()[0].second
            for(r in nouvelleListeReveils.values){
                if(r.nextAlarmCalendar.before(minR.nextAlarmCalendar)){
                    minR=r
                }
            }
            notification(""+minR.getText())
        } else {
            deleteNotification()
        }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (resultCode == Activity.RESULT_OK && data != null) {
            val newClock = data.extras!![ReveilModel.REVEIL] as ReveilModel
            Utility.createSimpleToast(ReveilModel.getTextNextClock(newClock.nextAlarmCalendar.timeInMillis))
            when (requestCode) {
                ReveilModel.CREATE_REQUEST_CODE -> {
                    if (resultCode == Activity.RESULT_OK) {
                        viewModel.addReveil(newClock, newClock.idReveil)
                    }
                }
                ReveilModel.EDIT_REQUEST_CODE -> {
                    if (data.hasExtra(ReveilModel.DELETE) && data.getBooleanExtra(
                            ReveilModel.DELETE,
                            true
                        )
                    ) {
                        viewModel.removeReveil(data.extras!![ReveilModel.NUM_REVEIL] as Int)
                    } else {
                        viewModel.editReveil(newClock, data.extras!![ReveilModel.NUM_REVEIL] as Int)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_reveil_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.list_reveil)
        adapter = ReveilListeAdapter(reveils, this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        view.bouton_add_reveil.setOnClickListener {
            val intent = Intent(activity, ReveilEdit::class.java)
            intent.putExtra("requestCode", ReveilModel.CREATE_REQUEST_CODE)
            startActivityForResult(
                intent,
                ReveilModel.CREATE_REQUEST_CODE
            )
        }

        requireActivity().setTitle("Réveils")
        return view
    }


    override fun onReveilClicked(reveilModel: ReveilModel, itemView: View) {

        val intent = Intent(activity, ReveilEdit::class.java)
        intent.putExtra(ReveilModel.NUM_REVEIL, reveilModel.idReveil)
        intent.putParcelableExtra(
            ReveilModel.REVEIL, reveilModel
        )
        intent.putExtra("requestCode", ReveilModel.EDIT_REQUEST_CODE)
        startActivityForResult(
            intent,
            ReveilModel.EDIT_REQUEST_CODE
        )
    }

    override fun onReveilSwitched(reveilModel: ReveilModel) {
        viewModel.switchReveil(reveilModel.idReveil)
    }

    override fun onReveilDelete(reveilModel: ReveilModel) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Supprimer réveil")
        builder.setMessage("Voulez-vous supprimer ce réveil ?")
        builder.setPositiveButton("Supprimer") { dialog, which ->
            viewModel.removeReveil(reveilModel.idReveil)
        }
        builder.setNeutralButton("Annuler") { _, _ -> }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }



    private fun notification(textProchainReveil : String){

        lateinit var notificationChannel : NotificationChannel
        lateinit var builder : Notification
        val channelId = "com.vguivarc.wakemeup.notification"
        val description = "Music Me ! Alarme prévue ...(TODO add texte)"
        val notificationManage =  requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val main_activity_intent = Intent(requireActivity(), MainActivity::class.java)
        val pi = PendingIntent.getActivity(requireContext(),0,main_activity_intent,0)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

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
        }
        else{
            builder = Notification.Builder(requireContext())
                .setContentTitle("Votre réveil social sonne!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Clique sur moi!")
                .setContentIntent(pi)
                .build()
        }
        notificationManage.notify(0,builder)
    }


    private fun deleteNotification() {
        val notificationManage =  requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManage.cancelAll()
    }

}