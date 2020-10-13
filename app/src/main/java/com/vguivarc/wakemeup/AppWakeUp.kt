package com.vguivarc.wakemeup

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.vguivarc.wakemeup.repo.Repository
import com.vguivarc.wakemeup.reveil.ReveilSonneService.Companion.CHANNEL_ID
import com.vguivarc.wakemeup.reveil.adds.AlarmSetter
import timber.log.Timber


class AppWakeUp : Application() {


    // TODO notification ajout en ami / envoie de musique / réveille quelqu'un
    // TODO volume sonnerie progressif
    // TODO "passer" illisible dans le tuto qu'il faut aussi finir
    // TODO si pas internet on est pas connecté
    // TODO nom devient un lien dans attente, comme pour passé
    // TODO paramétrage (durée snooze, etc)
    // TODO système de notif...
    // TODO choisir sa sonnerie par défaut
    // TODO voir le cas où on refuse l'accès aux amis, que ça marche et qu'on puisse accepter après coup
    // TODO ajouter liste chansons envoyées à chaque utilisateur


    companion object {
/*
        fun makeToastShort(s : String){
            Toast.makeText(appContext, s, Toast.LENGTH_SHORT).show()
        }
*/

        lateinit var repository: Repository
        const val NAME_FILE_REVEIL = "clock_list.file"
        const val NAME_FILE_HISTORIQUE = "historique.file"


        lateinit var appContext: Context

        lateinit var  alarmSetterImpl : AlarmSetter.AlarmSetterImpl

        fun userMessageRegistration() {
            if(FirebaseAuth.getInstance().currentUser==null){
                Timber.e("no user !")
            } else {
                Timber.e("there is a user ! ${FirebaseAuth.getInstance().currentUser!!.uid}")
                FirebaseMessaging.getInstance().subscribeToTopic("user_"+FirebaseAuth.getInstance().currentUser!!.uid)
                    .addOnCompleteListener { task ->
                        Timber.e("user registration : ${task.isSuccessful}")
                        Timber.e("user registration : ${"user_"+FirebaseAuth.getInstance().currentUser!!.uid}")
                    }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        appContext = applicationContext
        val notificationManage =  appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannelReveil =
                NotificationChannel(
                    CHANNEL_ID,
                    "notificationChannelReveil",
                    NotificationManager.IMPORTANCE_HIGH
                )
            notificationManage.createNotificationChannel(notificationChannelReveil)
        }
        alarmSetterImpl = AlarmSetter.AlarmSetterImpl(appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager, appContext)
        repository = Repository()
        repository.chargement()
    }



}