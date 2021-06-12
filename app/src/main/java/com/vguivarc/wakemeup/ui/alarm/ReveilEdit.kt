package com.vguivarc.wakemeup.ui.alarm

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.entity.Alarm
import com.vguivarc.wakemeup.util.putParcelableExtra
import kotlinx.android.synthetic.main.activity_reveil_edit.*
import java.util.*

class ReveilEdit : AppCompatActivity() {

    private lateinit var alarm: Alarm


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
            val days = arrayOf(
                "Ne pas répéter",
                "Du lundi au vendredi",
                "Tous les jours",
                "Personnaliser"
            )

            val checkedItem = alarm.repetition.ordinal
            builder.setSingleChoiceItems(days, checkedItem) { dialog, which ->
                when (which) {
                    0 -> {
                        alarm.repetition = Alarm.ReveilMode.PasDeRepetition
                    }
                    1 -> {
                        alarm.listActifDays = mutableListOf(
                            Alarm.DaysWeek.Lundi,
                            Alarm.DaysWeek.Mardi,
                            Alarm.DaysWeek.Mercredi,
                            Alarm.DaysWeek.Jeudi,
                            Alarm.DaysWeek.Vendredi
                        )
                        alarm.repetition = Alarm.ReveilMode.Semaine
                    }
                    2 -> {
                        alarm.listActifDays = mutableListOf(
                            Alarm.DaysWeek.Lundi,
                            Alarm.DaysWeek.Mardi,
                            Alarm.DaysWeek.Mercredi,
                            Alarm.DaysWeek.Jeudi,
                            Alarm.DaysWeek.Vendredi,
                            Alarm.DaysWeek.Samedi,
                            Alarm.DaysWeek.Dimanche
                        )
                        alarm.repetition = Alarm.ReveilMode.TousLesJours
                    }
                    3 -> {
                        // setup the alert builder
                        val builder2 = AlertDialog.Builder(this)
                        builder2.setTitle("Jours de la semaine")
                        val listDaySelected = mutableListOf<Alarm.DaysWeek>()
                        if (alarm.repetition != Alarm.ReveilMode.Personnalise) {
                            listDaySelected.add(Alarm.DaysWeek.Lundi)
                            listDaySelected.add(Alarm.DaysWeek.Mardi)
                            listDaySelected.add(Alarm.DaysWeek.Mercredi)
                            listDaySelected.add(Alarm.DaysWeek.Jeudi)
                            listDaySelected.add(Alarm.DaysWeek.Vendredi)
                        } else {
                            listDaySelected.addAll(alarm.listActifDays)
                        }
                        alarm.repetition = Alarm.ReveilMode.Personnalise

                        // add a checkbox list
                        val daysWeek = arrayOf(
                            Alarm.DaysWeek.Lundi.name,
                            Alarm.DaysWeek.Mardi.name,
                            Alarm.DaysWeek.Mercredi.name,
                            Alarm.DaysWeek.Jeudi.name,
                            Alarm.DaysWeek.Vendredi.name,
                            Alarm.DaysWeek.Samedi.name,
                            Alarm.DaysWeek.Dimanche.name
                        )

                        val checkedItems =
                            booleanArrayOf(false, false, false, false, false, false, false)
                        for (d in Alarm.DaysWeek.values()) {
                            checkedItems[d.ordinal] = listDaySelected.contains(d)
                        }

                        builder2.setMultiChoiceItems(daysWeek, checkedItems) { _, which2, isChecked ->
                            if (isChecked) {
                                listDaySelected.add(Alarm.DaysWeek.values()[which2])
                            } else {
                                listDaySelected.remove(Alarm.DaysWeek.values()[which2])
                            }
                        }

                        // add OK and Cancel buttons
                        builder2.setPositiveButton("OK") { _, _ ->
                            alarm.listActifDays = listDaySelected
                            adaptTextDays(alarm)
                        }
                        //todo text string
                        builder2.setNegativeButton("Annuler") { _, _ ->
                            val dialog3 = builder.create()
                            dialog3.show()
                        }

                        // create and show the alert dialog
                        val dialog2 = builder2.create()
                        dialog2.show()
                        adaptTextDays(alarm)
                    }
                }
                adaptTextDays(alarm)
                dialog.dismiss()
            }

            // create and show the alert dialog
            val dialog = builder.create()
            dialog.show()
        }
        when (intent.getIntExtra("requestCode", -1)) {
            Alarm.CREATE_REQUEST_CODE -> {
                alarm = Alarm()
                bouton_editer_reveil_valider.setText(R.string.ajouter_r_veil)
                bouton_editer_reveil_valider.setOnClickListener {
                    alarm.calculeNextCalendarWithNewHours(edit_time_picker.hour, edit_time_picker.minute)
                    intent.putParcelableExtra(Alarm.REVEIL, alarm)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                bouton_editer_reveil_annuler.setText(R.string.annuler)
                bouton_editer_reveil_annuler.setOnClickListener {
                    setResult(Activity.RESULT_CANCELED, intent)
                    finish()
                }
            }
            Alarm.EDIT_REQUEST_CODE -> {
                alarm = intent.getSerializableExtra(Alarm.REVEIL) as Alarm
                bouton_editer_reveil_valider.setText(R.string.modifier_r_veil)
                bouton_editer_reveil_valider.setOnClickListener {
                    alarm.calculeNextCalendarWithNewHours(edit_time_picker.hour, edit_time_picker.minute)
                    intent.putParcelableExtra(Alarm.REVEIL, alarm)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                bouton_editer_reveil_annuler.setText(R.string.supprimer_r_veil)
                bouton_editer_reveil_annuler.setOnClickListener {
                    setResult(Activity.RESULT_OK, intent)
                    intent.putParcelableExtra(Alarm.REVEIL, alarm)
                    intent.putExtra(Alarm.DELETE, true)
                    finish()
                }
            }
            else -> {
                return
            }
        }

        edit_time_picker.hour = alarm.nextAlarmCalendar.get(Calendar.HOUR_OF_DAY)
        edit_time_picker.minute = alarm.nextAlarmCalendar.get(Calendar.MINUTE)
        adaptTextDays(alarm)
    }


    private fun adaptTextDays(reveil: Alarm) {
        when (reveil.repetition) {
            Alarm.ReveilMode.PasDeRepetition -> textEditReveilJoursChoisis.text = "Pas de répétition"
            Alarm.ReveilMode.Semaine -> textEditReveilJoursChoisis.text = "Du lundi au vendredi"
            Alarm.ReveilMode.TousLesJours -> textEditReveilJoursChoisis.text = "Tous les jours"
            Alarm.ReveilMode.Personnalise -> {
                var txt = ""
                for (d in reveil.listActifDays) {
                    txt = txt + d.name + ", "
                }
                txt = if (txt != "") {
                    txt.substring(0, txt.length - 2)
                } else {
                    "Jamais"
                }
                textEditReveilJoursChoisis.text = txt
            }
        }
    }




}
