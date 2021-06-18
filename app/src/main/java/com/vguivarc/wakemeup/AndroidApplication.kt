package com.vguivarc.wakemeup

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.vguivarc.wakemeup.data.repository.Repository
import com.vguivarc.wakemeup.di.networkModule
import com.vguivarc.wakemeup.di.servicesModule
import com.vguivarc.wakemeup.di.viewModelModule
import com.vguivarc.wakemeup.domain.service.AlarmAndroidSetter
import com.vguivarc.wakemeup.ui.alarm.ReveilSonneService.Companion.CHANNEL_ID
import io.paperdb.Paper
import io.reactivex.internal.functions.Functions
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class AndroidApplication : Application() {

    // TODO envoie de musique / réveille quelqu'un
    // TODO volume sonnerie progressif
    // TODO "passer" illisible dans le tuto qu'il faut aussi finir
    // TODO si pas internet on est pas connecté
    // TODO nom devient un lien dans attente, comme pour passé
    // TODO paramétrage (durée snooze, etc)
    // TODO système de notif...
    // TODO choisir sa sonnerie par défaut
    // TODO voir le cas où on refuse l'accès aux amis, que ça marche et qu'on puisse accepter après coup
    // TODO ajouter liste chansondroid.permission.WAKE_LOCKns envoyées à chaque utilisateur
    // TODO onglet "Musiques envoyées" (avec tous les envoies en vrac)
    // TODO outil recherche, le "partage", ya pas les nombres d'envoies pour les amis
    // TODO les notifs repassent à rouge quand il y a une musique en moins, et pas que quand il y en a plus

    companion object {
        // / var mFirebaseCrashlytics: FirebaseCrashlytics

        // TODO delete la suite
        lateinit var repository: Repository
        const val NAME_FILE_HISTORIQUE = "historique.file"

        lateinit var appContext: Context

        lateinit var alarmAndroidSetterImpl: AlarmAndroidSetter.AlarmAndroidSetterImpl

        fun userMessageRegistration() {
            if (FirebaseAuth.getInstance().currentUser == null) {
                Timber.e("no user !")
            } else {
                Timber.e("there is a user ! ${FirebaseAuth.getInstance().currentUser!!.uid}")
                FirebaseMessaging.getInstance().subscribeToTopic("user_" + FirebaseAuth.getInstance().currentUser!!.uid)
                    .addOnCompleteListener { task ->
                        Timber.e("user registration : ${task.isSuccessful}")
                        Timber.e("user registration : ${"user_" + FirebaseAuth.getInstance().currentUser!!.uid}")
                    }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        super.onCreate()
        initLogging()

//        mFirebaseCrashlytics = FirebaseCrashlytics.getInstance()

//        if (BuildConfig.DEBUG) mFirebaseCrashlytics?.setCrashlyticsCollectionEnabled(false)

        /**
         * By default, RXJava2 compels to handle an error thrown by the subscriber (i.e
         * our viewModel must receive the error and must not be disposed at this time).
         * If not, RXJava crashes the application with an UndeliverableException.
         * This line tells RxJavaPlugin to do nothing in that case.
         */
        RxJavaPlugins.setErrorHandler(Functions.emptyConsumer())

        startKoin {
            androidLogger()
            androidContext(this@AndroidApplication)
            koin.loadModules(
                listOf(
                    servicesModule,
                    viewModelModule,
                    networkModule
                )
            )
        }
        Paper.init(this)

        // TODO suite
        appContext = applicationContext
        val notificationManage = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannelReveil =
                NotificationChannel(
                    CHANNEL_ID,
                    "notificationChannelReveil",
                    NotificationManager.IMPORTANCE_HIGH
                )
            notificationManage.createNotificationChannel(notificationChannelReveil)
        }
        alarmAndroidSetterImpl = AlarmAndroidSetter.AlarmAndroidSetterImpl(appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager, appContext)
        repository = Repository()
    }

    private fun initLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
//            Timber.plant(CrashReportingTree())
        }
    }

    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
//            val crashlytics = FirebaseCrashlytics.getInstance()
            //          crashlytics.log(message)
            if (throwable != null) {
                //            crashlytics.recordException(throwable)
            }
        }
    }
}
