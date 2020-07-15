package com.vguivarc.wakemeup.song

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import androidx.fragment.app.FragmentActivity
import com.vguivarc.wakemeup.MainActivity
import com.vguivarc.wakemeup.R

//TODO Fait crash quand on utilise cette classe
/*
class DialogueYoutube(val activity: FragmentActivity) {

    private fun startActivityListFriendToSendMusicActivity(currentSong : Song?){
        //ouvre l'activity de la liste d'amis
        activity.intent = Intent(activity, ListFriendToSendMusicActivity::class.java)
        activity.intent.putExtra("song", currentSong as Parcelable)
        activity.startActivity(activity.intent)
    }


    fun createDialoguePartage(currentSong : Song?){
        val builder = AlertDialog.Builder(activity)
        val viewDialogPartage = activity.layoutInflater.inflate(R.layout.dialog_partage, null)

        val btChoix = viewDialogPartage.findViewById<Button>(R.id.bouton_choix)
        val btDebut = viewDialogPartage.findViewById<Button>(R.id.bouton_debut)

        val dialoguePartage =
            builder
                .setTitle("A quel moment voulez vous que la vidéo se lance?")
                .setView(viewDialogPartage)
                .setNegativeButton("Annuler"){ _, _ ->
            }.create()

        btChoix.setOnClickListener {
            createDialogueChoixDuTemps(currentSong)
            dialoguePartage.hide()
        }
        btDebut.setOnClickListener {
            currentSong?.lancement = 0
            startActivityListFriendToSendMusicActivity(currentSong)
            dialoguePartage.hide()
        }

        dialoguePartage.show()

    }

    private fun createDialogueChoixDuTemps(currentSong : Song?){
        val builder = AlertDialog.Builder(activity)
        val view = activity.layoutInflater.inflate(R.layout.dialogue_choisir_le_temps, null)
        builder.setTitle("A quel moment voulez vous que la vidéo se lance?")
            .setView(view)
            .setPositiveButton("Ok") { _, _ ->
                val heure = view.findViewById<EditText>(R.id.et_choix_du_temps_heure).text.toString().trim()
                val minute = view.findViewById<EditText>(R.id.et_choix_du_temps_minute).text.toString().trim()
                val seconde = view.findViewById<EditText>(R.id.et_choix_du_temps_seconde).text.toString().trim()

                if (heure.isNotEmpty() && minute.isNotEmpty() && seconde.isNotEmpty()) {

                    if (minute.toInt() <= 60 && seconde.toInt() <= 60) {
                        val temps_en_secondes: Int = 60 * 60 * heure.toInt() + 60 * minute.toInt() + seconde.toInt()
                        currentSong?.lancement = temps_en_secondes
                        startActivityListFriendToSendMusicActivity(currentSong)
                    } else {
                        Toast.makeText(
                            activity.application,
                            "Veuillez mettre un temps valide.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                else{
                    Toast.makeText(
                        activity.application,
                        "Veuillez remplir les champs.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
            .create().show()
    }


    fun createAlertDialogNotConnected(context: Context, ma: MainActivity) {
        // Initialize a new instance of
        val builder = AlertDialog.Builder(context)

        // Set the alert dialog title
        builder.setTitle("Connexion en attente")

        // Display a message on alert dialog
        builder.setMessage("Pour accéder à toutes les fonctionnalités de l'application, veuillez vous connecter.")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Se connecter") { _, _ ->
            ma.startConnectActivity(false)
        }
        // Display a neutral button on alert dialog
        builder.setNeutralButton("Annuler") { _, _ -> }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

}*/