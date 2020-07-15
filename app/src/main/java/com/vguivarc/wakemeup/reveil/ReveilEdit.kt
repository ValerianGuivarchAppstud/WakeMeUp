package com.vguivarc.wakemeup.reveil

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.MainActivity
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.util.putParcelableExtra
import kotlinx.android.synthetic.main.activity_reveil_edit.*
import java.util.*

class ReveilEdit : AppCompatActivity() {

    private lateinit var reveilModel: ReveilModel
    private var boolEdit = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_reveil_edit)
        edit_time_picker.setIs24HourView(true)

        if (intent.extras == null || !(intent.extras!!.containsKey("requestCode")))
            return


        bouton_editer_reveil_jours.setOnClickListener {
            // setup the alert builder
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Répétition du réveil")

            // add a radio button list
            //todo mettre en string.xml
            val days = arrayOf<String>(
                "Ne pas répéter",
                "Du lundi au vendredi",
                "Tous les jours",
                "Personnaliser"
            )

            val checkedItem = reveilModel.repetition.ordinal
            builder.setSingleChoiceItems(days, checkedItem) { dialog, which ->
                if (which == 0) {
                    reveilModel.repetition = ReveilModel.ReveilMode.PasDeRepetition
                } else if (which == 1) {
                    reveilModel.listActifDays = mutableListOf(
                        ReveilModel.DaysWeek.Lundi,
                        ReveilModel.DaysWeek.Mardi,
                        ReveilModel.DaysWeek.Mercredi,
                        ReveilModel.DaysWeek.Jeudi,
                        ReveilModel.DaysWeek.Vendredi
                    )
                    reveilModel.repetition = ReveilModel.ReveilMode.Semaine
                } else if (which == 2) {
                    reveilModel.listActifDays = mutableListOf(
                        ReveilModel.DaysWeek.Lundi,
                        ReveilModel.DaysWeek.Mardi,
                        ReveilModel.DaysWeek.Mercredi,
                        ReveilModel.DaysWeek.Jeudi,
                        ReveilModel.DaysWeek.Vendredi,
                        ReveilModel.DaysWeek.Samedi,
                        ReveilModel.DaysWeek.Dimanche
                    )
                    reveilModel.repetition = ReveilModel.ReveilMode.TousLesJours
                } else if (which == 3) {
                    // setup the alert builder
                    val builder2 = AlertDialog.Builder(this)
                    builder2.setTitle("Jours de la semaine")
                    val listDaySelected = mutableListOf<ReveilModel.DaysWeek>()
                    if (reveilModel.repetition != ReveilModel.ReveilMode.Personnalise) {
                        listDaySelected.add(ReveilModel.DaysWeek.Lundi)
                        listDaySelected.add(ReveilModel.DaysWeek.Mardi)
                        listDaySelected.add(ReveilModel.DaysWeek.Mercredi)
                        listDaySelected.add(ReveilModel.DaysWeek.Jeudi)
                        listDaySelected.add(ReveilModel.DaysWeek.Vendredi)
                    } else {
                        listDaySelected.addAll(reveilModel.listActifDays)
                    }
                    reveilModel.repetition = ReveilModel.ReveilMode.Personnalise

                    // add a checkbox list
                    val daysWeek = arrayOf(
                        ReveilModel.DaysWeek.Lundi.name,
                        ReveilModel.DaysWeek.Mardi.name,
                        ReveilModel.DaysWeek.Mercredi.name,
                        ReveilModel.DaysWeek.Jeudi.name,
                        ReveilModel.DaysWeek.Vendredi.name,
                        ReveilModel.DaysWeek.Samedi.name,
                        ReveilModel.DaysWeek.Dimanche.name
                    )

                    val checkedItems =
                        booleanArrayOf(false, false, false, false, false, false, false)
                    for (d in ReveilModel.DaysWeek.values()) {
                        checkedItems[d.ordinal] = listDaySelected.contains(d)
                    }

                    builder2.setMultiChoiceItems(daysWeek, checkedItems) { _, which2, isChecked ->
                        if (isChecked) {
                            listDaySelected.add(ReveilModel.DaysWeek.values()[which2])
                        } else {
                            listDaySelected.remove(ReveilModel.DaysWeek.values()[which2])
                        }
                    }

                    // add OK and Cancel buttons
                    builder2.setPositiveButton("OK") { _, _ ->
                        reveilModel.listActifDays = listDaySelected
                        adaptTextDays(reveilModel)
                    }
                    //todo text string
                    builder2.setNegativeButton("Annuler") { _, _ ->
                        val dialog3 = builder.create()
                        dialog3.show()
                    }

                    // create and show the alert dialog
                    val dialog2 = builder2.create()
                    dialog2.show()
                    adaptTextDays(reveilModel)
                }
                adaptTextDays(reveilModel)
                dialog.dismiss()
            }

            // create and show the alert dialog
            val dialog = builder.create()
            dialog.show()
        }
        when (intent.getIntExtra("requestCode", -1)) {
            ReveilModel.CREATE_REQUEST_CODE -> {
                reveilModel = ReveilModel()
                bouton_editer_reveil_valider.setText(R.string.ajouter_r_veil)
                bouton_editer_reveil_valider.setOnClickListener {
                    reveilModel.calculeNextCalendarWithNewHours(edit_time_picker.hour, edit_time_picker.minute)
                    intent.putParcelableExtra(ReveilModel.REVEIL, reveilModel)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                bouton_editer_reveil_annuler.setText(R.string.annuler)
                bouton_editer_reveil_annuler.setOnClickListener {
                    setResult(Activity.RESULT_CANCELED, intent)
                    finish()
                }
            }
            ReveilModel.EDIT_REQUEST_CODE -> {
                reveilModel = intent.getSerializableExtra(ReveilModel.REVEIL) as ReveilModel
                bouton_editer_reveil_valider.setText(R.string.modifier_r_veil)
                bouton_editer_reveil_valider.setOnClickListener {
                    reveilModel.calculeNextCalendarWithNewHours(edit_time_picker.hour, edit_time_picker.minute)
                    intent.putParcelableExtra(ReveilModel.REVEIL, reveilModel)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                bouton_editer_reveil_annuler.setText(R.string.supprimer_r_veil)
                bouton_editer_reveil_annuler.setOnClickListener {
                    setResult(Activity.RESULT_OK, intent)
                    intent.putParcelableExtra(ReveilModel.REVEIL, reveilModel)
                    intent.putExtra(ReveilModel.DELETE, true)
                    finish()
                }
            }
            else -> {
                return
            }
        }

        edit_time_picker.hour = reveilModel.nextAlarmCalendar.get(Calendar.HOUR_OF_DAY)
        edit_time_picker.minute = reveilModel.nextAlarmCalendar.get(Calendar.MINUTE)
        adaptTextDays(reveilModel)
    }


    private fun adaptTextDays(reveil: ReveilModel) {
        when (reveil.repetition) {
            ReveilModel.ReveilMode.PasDeRepetition -> textEditReveilJoursChoisis.setText("Pas de répétition")
            ReveilModel.ReveilMode.Semaine -> textEditReveilJoursChoisis.setText("Du lundi au vendredi")
            ReveilModel.ReveilMode.TousLesJours -> textEditReveilJoursChoisis.setText("Tous les jours")
            ReveilModel.ReveilMode.Personnalise -> {
                var txt = ""
                for (d in reveil.listActifDays) {
                    txt = txt + d.name + ", "
                }
                if (txt != "") {
                    txt = txt.substring(0, txt.length - 2)
                } else {
                    txt = "Jamais"
                }
                textEditReveilJoursChoisis.setText(txt)
            }
        }
    }




}
