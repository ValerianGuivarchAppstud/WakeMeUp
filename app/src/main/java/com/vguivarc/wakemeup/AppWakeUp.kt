package com.vguivarc.wakemeup

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.vguivarc.wakemeup.connect.UserModel
import com.vguivarc.wakemeup.repo.Repository
import timber.log.Timber


class AppWakeUp : Application() {

    //TODO tout ça !
    // changer photo de profil
    // notification ajout en ami / envoie de musique / réveille quelqu'un
    // volume sonnerie progressif
    // "passer" illisible dans le tuto qu'il faut aussi finir
    // si non connecté, ça marche très mal au lancement
    // corriger
    // nom devient un lien dans attente, comme pour passé
    // logo "empty_profil" en couleur et pas blanc sinon pâs visible
    // profiter de la création de link pour supprimer ceux vieux de plus de X jours
    // paramétrage (durée snooze, etc)
    // système de notif...
    // choisir sa sonnerie par défaut
    // lien FB marche pas
    // TODO voir le cas où on refuse l'accès aux amis, que ça marche et qu'on puisse accepter après coup


    companion object {

        fun makeToastShort(s : String){
            Toast.makeText(appContext, s, Toast.LENGTH_SHORT).show()
        }


        lateinit var repository: Repository
        const val NAME_FILE_REVEIL = "clock_list.file"
        const val NAME_FILE_HISTORIQUE = "historique.file"
        lateinit var listeAmis: MutableList<UserModel>


        lateinit var appContext: Context
        const val NOTIFICATION_CHANNEL_ID = "WakeMeUp"
        const val NOTIFICATION_Test = 0
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        //AppEventsLogger.activateApp(this)


        appContext = applicationContext
        repository = Repository()
        repository.chargement()

    }

}