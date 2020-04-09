package com.wakemeup.alarmclock

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.wakemeup.R
import com.wakemeup.putParcelableExtra
import kotlinx.android.synthetic.main.fragment_reveil.view.*

class ReveilFragment : Fragment() {


    companion object {
        fun newInstance(): ReveilFragment {
            return ReveilFragment()
        }
    }

    private lateinit var fragmentView: View
    private var fragmentContainer: ViewGroup? = null


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {

            val newClock = data.extras!![Reveil.REVEIL] as Reveil

            when (requestCode) {
                Reveil.CREATE_REQUEST_CODE -> {
                    if (resultCode == Activity.RESULT_OK) {
                        Log.e("ADD", "new -> " + newClock.idReveil)
                        newClock.startAlarm(activity!!)
                        alarmViewModel.addReveil(newClock, newClock.idReveil)
                    }
                }
                Reveil.EDIT_REQUEST_CODE -> {
                    if (data.hasExtra(Reveil.DELETE) && data.getBooleanExtra(Reveil.DELETE, true)) {
                        newClock.cancelAlarm(activity!!)
                        alarmViewModel.removeReveil(data.extras!![Reveil.NUM_REVEIL] as Int)
                    } else {
                        newClock.cancelAlarm(activity!!)
                        newClock.startAlarm(activity!!)
                        alarmViewModel.editReveil(newClock, data.extras!![Reveil.NUM_REVEIL] as Int)
                    }
                }
            }

            //= alarmViewModel.getReveils().value!![data.getIntExtra(Reveil.REVEIL, 0)]
            //addReveilItem(context!!, fragmentView, newClock, data.getIntExtra(Reveil.NUM_REVEIL, 0))

            //print toast

            newClock.startAlarm(activity!!)
        }
    }

    private lateinit var alarmViewModel: AlarmViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        fragmentView = inflater.inflate(R.layout.fragment_reveil, container, false)

        alarmViewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)


        alarmViewModel.getReveils().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            fragmentView.list_reveil.removeAllViews()
            var index = 0
            for (reveil in it) {
                addReveilItem(context!!, fragmentView, reveil.value)
                index += 1
            }
        })

        fragmentContainer = container

        fragmentView.bouton_add_reveil.setOnClickListener {
            val intent = Intent(activity, ReveilEdit::class.java)

            intent.putExtra("requestCode", Reveil.CREATE_REQUEST_CODE)
            startActivityForResult(
                intent,
                Reveil.CREATE_REQUEST_CODE
            )
        }
        return fragmentView
    }


    private fun addReveilItem(context: Context, fragmentView: View, reveil: Reveil) {
        Log.e("RevItem", reveil.idReveil.toString())
        val item = LayoutInflater.from(context)
            .inflate(R.layout.item_reveil_programme, fragmentContainer, false)
        item.findViewById<TextView>(R.id.item_reveil_heure).text = reveil.getHeureTexte()
        item.findViewById<TextView>(R.id.item_reveil_jours).text = reveil.getJoursTexte()
        item.findViewById<Switch>(R.id.item_reveil_switch).isChecked = reveil.isActif
        item.findViewById<Switch>(R.id.item_reveil_switch).setOnClickListener {
            reveil.isActif = !reveil.isActif
            if (reveil.isActif) {
                reveil.startAlarm(activity!!)
            } else {
                reveil.cancelAlarm(activity!!)
            }
        }
        item.findViewById<ImageButton>(R.id.delete_clock).setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Supprimer réveil")
            builder.setMessage("Voulez-vous supprimer ce réveil ?")
            builder.setPositiveButton("Supprimer") { dialog, which ->
                reveil.cancelAlarm(activity!!)
                alarmViewModel.removeReveil(reveil.idReveil)
            }
            builder.setNeutralButton("Annuler") { _, _ -> }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        item.setOnClickListener {
            val intent = Intent(activity, ReveilEdit::class.java)
            intent.putExtra(Reveil.NUM_REVEIL, reveil.idReveil)
            intent.putParcelableExtra(
                Reveil.REVEIL,
                Parcelable@ (alarmViewModel.getReveils().value!![reveil.idReveil]!!)
            )
            intent.putExtra("requestCode", Reveil.EDIT_REQUEST_CODE)
            startActivityForResult(
                intent,
                Reveil.EDIT_REQUEST_CODE
            )
        }
        fragmentView.findViewById<LinearLayout>(R.id.list_reveil).addView(item)

    }
}