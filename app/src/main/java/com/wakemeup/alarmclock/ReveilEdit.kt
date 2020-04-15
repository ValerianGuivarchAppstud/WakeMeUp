package com.wakemeup.alarmclock

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.wakemeup.R
import com.wakemeup.putParcelableExtra
import kotlinx.android.synthetic.main.activity_reveil_edit.*
import java.util.*
import kotlin.system.exitProcess

class ReveilEdit : AppCompatActivity() {

    private lateinit var reveil: Reveil

    private val listDaysInWeek = listOf(
        Reveil.DaysWeek.Dimanche,
        Reveil.DaysWeek.Lundi,
        Reveil.DaysWeek.Mardi,
        Reveil.DaysWeek.Mercredi,
        Reveil.DaysWeek.Jeudi,
        Reveil.DaysWeek.Vendredi,
        Reveil.DaysWeek.Samedi
    )

    private fun dayButtonClicked(day: Reveil.DaysWeek, edit_liste_jour_button: ToggleButton) {
        if (reveil.listActifDays.contains(day)) {
            reveil.listActifDays.remove(day)
        } else {
            reveil.listActifDays.add(day)
        }
        dayButtonUpdate(day, edit_liste_jour_button)
    }

    private fun dayButtonUpdate(day: Reveil.DaysWeek, edit_liste_jour_button: ToggleButton) {
        if (reveil.listActifDays.contains(day)) {
            edit_liste_jour_button.alpha = 1f
        } else {
            edit_liste_jour_button.alpha = 0.3f
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_reveil_edit)
        edit_time_picker.setIs24HourView(true)



        if (intent.extras == null || !(intent.extras!!.containsKey("requestCode")))
            return

        when (intent.getIntExtra("requestCode", -1)) {
            Reveil.CREATE_REQUEST_CODE -> {
                reveil = Reveil()
                Log.e("ReveilEdit", "new -> " + reveil.idReveil)
                bouton_ajouter_reveil.setOnClickListener {
                    reveil.nextAlarm = calculeNextCalendar(reveil)
                    intent.putParcelableExtra(Reveil.REVEIL, reveil)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                bouton_annuler_ajout.setOnClickListener {
                    setResult(Activity.RESULT_CANCELED, intent)
                    finish()
                }
                bouton_editer_reveil.visibility = View.GONE
                bouton_supprimer_reveil.visibility = View.GONE
            }
            Reveil.EDIT_REQUEST_CODE -> {
                reveil = intent.getSerializableExtra(Reveil.REVEIL) as Reveil
                bouton_editer_reveil.setOnClickListener {
                    reveil.nextAlarm = calculeNextCalendar(reveil)
                    intent.putParcelableExtra(Reveil.REVEIL, reveil)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                bouton_supprimer_reveil.setOnClickListener {
                    setResult(Activity.RESULT_OK, intent)
                    intent.putParcelableExtra(Reveil.REVEIL, reveil)
                    intent.putExtra(Reveil.DELETE, true)
                    finish()
                }
                bouton_ajouter_reveil.visibility = View.GONE
                bouton_annuler_ajout.visibility = View.GONE
            }
            else -> {
                return
            }
        }

        edit_time_picker.hour = reveil.nextAlarm.get(Calendar.HOUR_OF_DAY)
        edit_time_picker.minute = reveil.nextAlarm.get(Calendar.MINUTE)
        dayButtonUpdate(Reveil.DaysWeek.Lundi, edit_liste_jour_lundi)
        dayButtonUpdate(Reveil.DaysWeek.Mardi, edit_liste_jour_mardi)
        dayButtonUpdate(Reveil.DaysWeek.Mercredi, edit_liste_jour_mercredi)
        dayButtonUpdate(Reveil.DaysWeek.Jeudi, edit_liste_jour_jeudi)
        dayButtonUpdate(Reveil.DaysWeek.Vendredi, edit_liste_jour_vendredi)
        dayButtonUpdate(Reveil.DaysWeek.Samedi, edit_liste_jour_samedi)
        dayButtonUpdate(Reveil.DaysWeek.Dimanche, edit_liste_jour_dimanche)

        edit_liste_jour_lundi.setOnClickListener {
            dayButtonClicked(Reveil.DaysWeek.Lundi, edit_liste_jour_lundi)
        }
        edit_liste_jour_mardi.setOnClickListener {
            dayButtonClicked(Reveil.DaysWeek.Mardi, edit_liste_jour_mardi)
        }
        edit_liste_jour_mercredi.setOnClickListener {
            dayButtonClicked(Reveil.DaysWeek.Mercredi, edit_liste_jour_mercredi)
        }
        edit_liste_jour_jeudi.setOnClickListener {
            dayButtonClicked(Reveil.DaysWeek.Jeudi, edit_liste_jour_jeudi)
        }
        edit_liste_jour_vendredi.setOnClickListener {
            dayButtonClicked(Reveil.DaysWeek.Vendredi, edit_liste_jour_vendredi)
        }
        edit_liste_jour_samedi.setOnClickListener {
            dayButtonClicked(Reveil.DaysWeek.Samedi, edit_liste_jour_samedi)
        }
        edit_liste_jour_dimanche.setOnClickListener {
            dayButtonClicked(Reveil.DaysWeek.Dimanche, edit_liste_jour_dimanche)
        }


    }


    private fun calculeNextCalendar(reveil: Reveil): Calendar {
        val now = Calendar.getInstance()
        val next = Calendar.getInstance()
        next.set(Calendar.HOUR_OF_DAY, edit_time_picker.hour)
        next.set(Calendar.MINUTE, edit_time_picker.minute)
        next.set(Calendar.SECOND, 0)
        next.set(Calendar.MILLISECOND, 0)
        if (reveil.listActifDays.isEmpty()) {
            Log.e("ReveilEdit", "ListActifDays empty")
            exitProcess(0)
        }


        while (now > next || !reveil.listActifDays.contains(listDaysInWeek[next.get(Calendar.DAY_OF_WEEK) - 1])) {
            next.set(Calendar.DAY_OF_YEAR, next.get(Calendar.DAY_OF_YEAR) + 1)
        }
        return next
    }
}
