package com.wakemeup

import android.app.Application
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.neocampus.repo.Repository
import com.wakemeup.connect.UserModel
import com.wakemeup.contact.SonnerieEnAttente
import com.wakemeup.contact.SonneriePassee
import java.io.ObjectOutputStream


class AppWakeUp : Application() {


    companion object {

        lateinit var repository: Repository
        lateinit var auth: FirebaseAuth
        lateinit var database: FirebaseDatabase

        const val NAME_FILE_REVEIL = "clock_list.file"
        private const val NAME_FILE_SONNERIES_EN_ATTENTE = "sonneries_en-attente.file"
        private const val NAME_FILE_SONNERIES_PASSEES = "sonneries_passees.file"
        lateinit var listeAmis: MutableList<UserModel>

        val listSonneriesEnAttente = mutableMapOf<String, SonnerieEnAttente>()

        val listSonneriesPassee = mutableMapOf<String, SonneriePassee>()


        fun addSonnerieEnAttente(
            idSonnerie: String,
            sonnerie: SonnerieEnAttente,
            context: Context
        ) {
            listSonneriesEnAttente.put(idSonnerie, sonnerie)
            enregistrementSonnerieEnAttente(context)
        }

        fun removeSonnerieEnAttente(idSonnerie: String, musicId: String, context: Context) {
            if (listSonneriesEnAttente.containsKey(idSonnerie)) {
                listSonneriesPassee.put(
                    idSonnerie,
                    SonneriePassee(listSonneriesEnAttente[idSonnerie]!!)
                )
                listSonneriesEnAttente.remove(idSonnerie)
                enregistrementSonneriePassees(context)
                enregistrementSonnerieEnAttente(context)
            }
        }

        private fun enregistrementSonnerieEnAttente(context: Context) {
            val fileOutput =
                context.openFileOutput(NAME_FILE_SONNERIES_EN_ATTENTE, Context.MODE_PRIVATE)
            val outputStream = ObjectOutputStream(fileOutput)
            outputStream.writeObject(listSonneriesEnAttente)
        }


        private fun enregistrementSonneriePassees(context: Context) {
            val fileOutput =
                context.openFileOutput(NAME_FILE_SONNERIES_PASSEES, Context.MODE_PRIVATE)
            val outputStream = ObjectOutputStream(fileOutput)
            outputStream.writeObject(listSonneriesPassee)
        }

        lateinit var appContext: Context
        lateinit var appActivity: Application
        const val NOTIFICATION_CHANNEL_ID = "WakeMeUp"
        const val NOTIFICATION_Test = 0
    }

    override fun onCreate() {
        super.onCreate()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        appContext = applicationContext
        repository = Repository()
    }
}