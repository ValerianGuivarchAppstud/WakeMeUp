package com.wakemeup.reveil

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.wakemeup.R
import com.wakemeup.util.putParcelableExtra
import kotlinx.android.synthetic.main.activity_reveil_edit.*
import java.util.*
import kotlin.system.exitProcess

class ReveilEdit : AppCompatActivity() {

    private lateinit var reveilModel: ReveilModel

    private val listDaysInWeek = listOf(
        ReveilModel.DaysWeek.Dimanche,
        ReveilModel.DaysWeek.Lundi,
        ReveilModel.DaysWeek.Mardi,
        ReveilModel.DaysWeek.Mercredi,
        ReveilModel.DaysWeek.Jeudi,
        ReveilModel.DaysWeek.Vendredi,
        ReveilModel.DaysWeek.Samedi
    )

    private fun dayButtonClicked(day: ReveilModel.DaysWeek, edit_liste_jour_button: ToggleButton) {
        if (reveilModel.listActifDays.contains(day)) {
            reveilModel.listActifDays.remove(day)
        } else {
            reveilModel.listActifDays.add(day)
        }
        dayButtonUpdate(day, edit_liste_jour_button)
    }

    private fun dayButtonUpdate(day: ReveilModel.DaysWeek, edit_liste_jour_button: ToggleButton) {
        if (reveilModel.listActifDays.contains(day)) {
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
            ReveilModel.CREATE_REQUEST_CODE -> {
                reveilModel = ReveilModel()
                Log.e("ReveilEdit", "new -> " + reveilModel.idReveil)
                bouton_ajouter_reveil.setOnClickListener {
                    reveilModel.nextAlarm = calculeNextCalendar(reveilModel)
                    intent.putParcelableExtra(ReveilModel.REVEIL, reveilModel)
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
            ReveilModel.EDIT_REQUEST_CODE -> {
                reveilModel = intent.getSerializableExtra(ReveilModel.REVEIL) as ReveilModel
                bouton_editer_reveil.setOnClickListener {
                    reveilModel.nextAlarm = calculeNextCalendar(reveilModel)
                    intent.putParcelableExtra(ReveilModel.REVEIL, reveilModel)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                bouton_supprimer_reveil.setOnClickListener {
                    setResult(Activity.RESULT_OK, intent)
                    intent.putParcelableExtra(ReveilModel.REVEIL, reveilModel)
                    intent.putExtra(ReveilModel.DELETE, true)
                    finish()
                }
                bouton_ajouter_reveil.visibility = View.GONE
                bouton_annuler_ajout.visibility = View.GONE
            }
            else -> {
                return
            }
        }

        edit_time_picker.hour = reveilModel.nextAlarm.get(Calendar.HOUR_OF_DAY)
        edit_time_picker.minute = reveilModel.nextAlarm.get(Calendar.MINUTE)
        dayButtonUpdate(ReveilModel.DaysWeek.Lundi, edit_liste_jour_lundi)
        dayButtonUpdate(ReveilModel.DaysWeek.Mardi, edit_liste_jour_mardi)
        dayButtonUpdate(ReveilModel.DaysWeek.Mercredi, edit_liste_jour_mercredi)
        dayButtonUpdate(ReveilModel.DaysWeek.Jeudi, edit_liste_jour_jeudi)
        dayButtonUpdate(ReveilModel.DaysWeek.Vendredi, edit_liste_jour_vendredi)
        dayButtonUpdate(ReveilModel.DaysWeek.Samedi, edit_liste_jour_samedi)
        dayButtonUpdate(ReveilModel.DaysWeek.Dimanche, edit_liste_jour_dimanche)

        edit_liste_jour_lundi.setOnClickListener {
            dayButtonClicked(ReveilModel.DaysWeek.Lundi, edit_liste_jour_lundi)
        }
        edit_liste_jour_mardi.setOnClickListener {
            dayButtonClicked(ReveilModel.DaysWeek.Mardi, edit_liste_jour_mardi)
        }
        edit_liste_jour_mercredi.setOnClickListener {
            dayButtonClicked(ReveilModel.DaysWeek.Mercredi, edit_liste_jour_mercredi)
        }
        edit_liste_jour_jeudi.setOnClickListener {
            dayButtonClicked(ReveilModel.DaysWeek.Jeudi, edit_liste_jour_jeudi)
        }
        edit_liste_jour_vendredi.setOnClickListener {
            dayButtonClicked(ReveilModel.DaysWeek.Vendredi, edit_liste_jour_vendredi)
        }
        edit_liste_jour_samedi.setOnClickListener {
            dayButtonClicked(ReveilModel.DaysWeek.Samedi, edit_liste_jour_samedi)
        }
        edit_liste_jour_dimanche.setOnClickListener {
            dayButtonClicked(ReveilModel.DaysWeek.Dimanche, edit_liste_jour_dimanche)
        }


    }


    private fun calculeNextCalendar(reveilModel: ReveilModel): Calendar {
        val now = Calendar.getInstance()
        val next = Calendar.getInstance()
        next.set(Calendar.HOUR_OF_DAY, edit_time_picker.hour)
        next.set(Calendar.MINUTE, edit_time_picker.minute)
        next.set(Calendar.SECOND, 0)
        next.set(Calendar.MILLISECOND, 0)
        if (reveilModel.listActifDays.isEmpty()) {
            Log.e("ReveilEdit", "ListActifDays empty")
            exitProcess(0)
        }


        while (now > next || !reveilModel.listActifDays.contains(listDaysInWeek[next.get(Calendar.DAY_OF_WEEK) - 1])) {
            next.set(Calendar.DAY_OF_YEAR, next.get(Calendar.DAY_OF_YEAR) + 1)
        }
        return next
    }
}
