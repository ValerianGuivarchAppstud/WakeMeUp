package com.wakemeup

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.neocampus.repo.Repository
import com.wakemeup.connect.UserModel
import com.wakemeup.contact.SonnerieRecue
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

        /**************************************************/
        /**************** SONNERIES RECUES ****************/
        /**************************************************/

        val listSonneriesEnAttente = mutableMapOf<String, SonnerieRecue>()
        val listSonneriesPassee = mutableMapOf<String, SonnerieRecue>()
        private val listSonneriesPasseeLiveData = MutableLiveData<Map<String, SonnerieRecue>>()
        private val listSonneriesEnAttenteLiveData = MutableLiveData<Map<String, SonnerieRecue>>()

        fun getSonneriesAttente(): LiveData<Map<String, SonnerieRecue>> = listSonneriesEnAttenteLiveData
        fun getSonneriesPassees(): LiveData<Map<String, SonnerieRecue>> = listSonneriesPasseeLiveData

        fun addSonnerieEnAttente(
            idSonnerie: String,
            sonnerie: SonnerieRecue,
            context: Context
        ) {
            listSonneriesEnAttente.put(idSonnerie, sonnerie)
            listSonneriesEnAttenteLiveData.value = listSonneriesEnAttente
        }

        fun removeSonnerieEnAttente(idSonnerie: String, musicId: String, context: Context) {
            if (listSonneriesEnAttente.containsKey(idSonnerie)) {
                listSonneriesPassee.put(
                    idSonnerie,
                    SonnerieRecue(listSonneriesEnAttente[idSonnerie]!!)
                )
                listSonneriesEnAttente.remove(idSonnerie)
                listSonneriesEnAttenteLiveData.value = listSonneriesEnAttente
                listSonneriesPasseeLiveData.value = listSonneriesPassee

                // todo passer le param ecoutee à true dans la bdd
            }
        }

        fun removeSonneriePassee(idSonnerie: String, musicId: String, context: Context) {
            if (listSonneriesPassee.containsKey(idSonnerie)) {
                listSonneriesPassee.remove(idSonnerie)
                listSonneriesPasseeLiveData.value = listSonneriesPassee

                // todo enlever la sonnerie dans la bdd
            }
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