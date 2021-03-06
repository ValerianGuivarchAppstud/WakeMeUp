package com.vguivarc.wakemeup.ui.ringingalarm

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.*
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.vguivarc.wakemeup.AndroidApplication
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.entity.Alarm
import com.vguivarc.wakemeup.domain.entity.Ringing
import com.vguivarc.wakemeup.domain.service.AlarmAndroidSetter.Companion.EXTRA_ID
import com.vguivarc.wakemeup.ui.sonnerie.SonnerieListeViewModel
import com.vguivarc.wakemeup.util.Utility
import com.vguivarc.wakemeup.viewmodel.AlarmsViewModel
import kotlinx.android.synthetic.main.activity_reveil_sonne.*
import timber.log.Timber
import java.util.*

/**
 * Reveil sonne activity
 *
 * @constructor Create empty Reveil sonne activity
 */
class RingingAlarmActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private val loading = MutableLiveData<Boolean>()

    private lateinit var audioManager: AudioManager
    private var initialVolume = 0

    private var threadVolumeProgressif: Thread? = null
    private var threadNoMusic: Thread? = null

    private fun dealWithNoMusic(): Thread {
        class WorkerDealWithNoMusic(private val handler: Handler) : Runnable {
            override fun run() {
                try {
                    Thread.sleep(10_000)
                    handler.sendEmptyMessage(0)
                } catch (e: InterruptedException) {
                }
            }
        }

        val handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                threadVolumeProgressif = dealWithVolume()
                val resID = AndroidApplication.appContext.resources.getIdentifier("sonnerie_default1", "raw", AndroidApplication.appContext.packageName)
                mediaPlayer = MediaPlayer.create(AndroidApplication.appContext, resID)
                    /* audioManager.setStreamVolume(
                         AudioManager.STREAM_MUSIC, // Stream type
                         audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Volume index
                         AudioManager.FLAG_SHOW_UI// Flags
                     )*/

                mediaPlayer!!.start()
                textPasDeMusiqueAttente.visibility = View.VISIBLE
                senderView.visibility = View.INVISIBLE
                loading.postValue(false)
            }
        }

        val workerDealWithNoMusic = WorkerDealWithNoMusic(handler)
        val thread = Thread(workerDealWithNoMusic)
        thread.start()
        return thread
    }

    private fun dealWithVolume(): Thread {
        class WorkerDealWithVolume(private val handler: Handler) : Runnable {
            override fun run() {
                try {
                    for (progressionVolume in 0..100) {
                        /*val newVol = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) - audioManager.getStreamMinVolume(
                                AudioManager.STREAM_MUSIC
                            ) / 10) * progressionVolume
                        } else {
                            (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 10) * progressionVolume
                        }*/
                        val msg = Message.obtain()
                        msg.arg1 = progressionVolume
                        handler.sendMessage(msg)
                        Thread.sleep(2_00)
                    }
                } catch (e: InterruptedException) {
                }
            }
        }

        val handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                    /*audioManager.setStreamVolume(
                        AudioManager.STREAM_MUSIC, // Stream type
                        msg.arg1, // Volume index
                        AudioManager.FLAG_SHOW_UI// Flags
                    )*/
                youTubePlayer!!.setVolume(msg.arg1)
            }
        }

        val workerDealWithVolume = WorkerDealWithVolume(handler)
        val thread = Thread(workerDealWithVolume)
        thread.start()
        return thread
    }

/*    class SonnerieAlarmWorker(val context: Context, params: WorkerParameters) : Worker(context, params) {
        override fun doWork(): Result {
           /* val audioManager = context.getSystemService(AUDIO_SERVICE) as AudioManager
            for(progressionVolume in 0..10){
                val newVol = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) - audioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC) / 10) * progressionVolume
                } else {
                    (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 10) * progressionVolume
                }
                Timber.e("volume="+newVol)
                audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC, // Stream type
                    newVol, // Volume index
                    AudioManager.FLAG_SHOW_UI// Flags
                )
                Thread.sleep(3_000)
            }*/
            return Result.success()
        }
    }*/
/*
    class SonnerieVolumeAlarmWorker(val context: Context, params: WorkerParameters) : Worker(context, params) {
        override fun doWork(): Result {
            Timber.e("lol")
            return Result.success()
        }
    }*/

    private var youTubePlayer: YouTubePlayer? = null
    private lateinit var youTubePlayerView: YouTubePlayerView
    var currentRinging: Ringing? = null

    private lateinit var viewModelSonnerie: SonnerieListeViewModel
    private lateinit var loadingView: ProgressBar
    private lateinit var senderView: TextView
    private lateinit var viewModelReveil: AlarmsViewModel
    private lateinit var textPasDeMusiqueAttente: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reveil_sonne)
        youTubePlayerView = findViewById(R.id.youtubePlayReveil)
        loadingView = findViewById(R.id.id_loading_sonnerie)
        senderView = findViewById(R.id.sender_sonnerie)
        textPasDeMusiqueAttente = findViewById(R.id.text_pas_de_musique_attente)
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        initialVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        loading.observe(
            this,
            {
                if (it) {
                    loadingView.visibility = View.VISIBLE
                } else {
                    loadingView.visibility = View.GONE
                }
            }
        )
        loading.value = true
        youTubePlayerView.visibility = View.INVISIBLE

     /*   val request = OneTimeWorkRequest.Builder(SonnerieAlarmWorker::class.java)
            .setInitialDelay(10000, TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(AppWakeUp.appContext)
            .beginUniqueWork("sonnerieParDefaut", ExistingWorkPolicy.REPLACE, request)
            .enqueue()


        WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.id).observe(this, { workStatus->
            if (workStatus != null && workStatus.state.isFinished) {
                if(youTubePlayerView.visibility!=View.VISIBLE) {
                    val resID = this.resources
                        .getIdentifier("sonnerie_default1", "raw", this.packageName)
                    mediaPlayer = MediaPlayer.create(this, resID)
                   /* audioManager.setStreamVolume(
                        AudioManager.STREAM_MUSIC, // Stream type
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Volume index
                        AudioManager.FLAG_SHOW_UI// Flags
                    )*/
                    volumeProgressif()
                    mediaPlayer!!.start()
                    textPasDeMusiqueAttente.visibility = View.VISIBLE
                    senderView.visibility = View.INVISIBLE
                    loading.postValue(false)
                }
            }
        })*/

        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)

        val powermanager =
            this.getSystemService(Context.POWER_SERVICE) as PowerManager

        val wakeLock = powermanager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "musicme:tag"
        )
        wakeLock.acquire(5 * 60 * 1000L /*10 minutes*/)

        if (wakeLock.isHeld) {
            wakeLock.release()
        }

        //  viewModelSonnerie.updateSonneries()

        threadNoMusic = dealWithNoMusic()

        viewModelSonnerie.getListeAttenteLiveData().observe(
            this,
            {
                if (it.isEmpty()) {
                    // pas de musique, ou pas encore charg?? donc on fait rien
                } else {
                    currentRinging = it.values.toMutableList()[0]
                    // Init du youTubePlayerView-----------------------------------------------------------------------
                    lifecycle.addObserver(youTubePlayerView)
                    Timber.e("ytPlayer")
                    senderView.text = this.getString(R.string.envoyee_par, currentRinging!!.senderName)

                /*audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC, // Stream type
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Volume index
                    AudioManager.FLAG_SHOW_UI// Flags
                )*/

                    youTubePlayerView.addYouTubePlayerListener(object :
                            AbstractYouTubePlayerListener() {
                            override fun onError(
                                youTubePlayer: YouTubePlayer,
                                error: PlayerConstants.PlayerError
                            ) {
                                super.onError(youTubePlayer, error)
                                Timber.e("Error YT %s", error.name)
                                Timber.e("Error YT %s", currentRinging)
                                Timber.e("Error YT %s", currentRinging!!.song!!.id)
                                // TODO musique par d??faut -> marche pas, error se lance pas si pas internet
                            }

                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                this@RingingAlarmActivity.youTubePlayer = youTubePlayer
                                Timber.e("Ready musique")
                                prepareSong(currentRinging!!)
                                youTubePlayerView.visibility = View.VISIBLE
                                loading.value = false
                                // WorkManager.getInstance(AppWakeUp.appContext).cancelAllWork()
                                threadNoMusic?.interrupt()
                                threadVolumeProgressif = dealWithVolume()
                            }
                        })
                    // -------------------------------------------------------------------------------------------------
                }
            }
        )

        // Initialisation des valeures de la date et heure----
        val calendar: Calendar = Calendar.getInstance()

        val jour = calendar.get(Calendar.DAY_OF_MONTH)
        val moisTxt = when (calendar.get(Calendar.MONTH) + 1) {
            1 -> "janvier"
            2 -> "f??vrier"
            3 -> "mars"
            4 -> "avril"
            5 -> "mai"
            6 -> "juin"
            7 -> "juillet"
            8 -> "ao??t"
            9 -> "septembre"
            10 -> "octobre"
            11 -> "novembre"
            else -> "d??cembre"
        }
        val annee = calendar.get(Calendar.YEAR)

        val heure = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        date_sonnerie.text = "$jour / $moisTxt / $annee"
        if (minute <10)
            heure_sonnerie.text = "$heure : 0$minute"
        else
            heure_sonnerie.text = "$heure : $minute"
        // -----------------------------------------------------

        // Gestion du bouton stop--------------------------------------
        bouton_reveil_stop.setOnClickListener {
            stop()
        }
        // ------------------------------------------------------------

        // Gestion du bouton snooze--------------------------------------------------------------
        bouton_reveil_snooze.setOnClickListener {
            snooze()
        }
        // --------------------------------------------------------------------------------------

        // TODO pas de texte de snooze car li?? ?? une activit?? qui s'arr??te juste apr??s
        // --------------------------------------------------------------------------------------
    }

    private fun stop() {
        threadVolumeProgressif?.interrupt()
        threadNoMusic?.interrupt()

        // WorkManager.getInstance(this).getWorkInfoById(requestVolume!!.id).cancel(true)
        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC, // Stream type
            initialVolume, // Volume index
            AudioManager.FLAG_SHOW_UI // Flags
        )

        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
        }
        val idReveil = intent!!.getIntExtra(EXTRA_ID, -1)
        Timber.e("Stop : %s", idReveil.toString())
        if (idReveil != -1) {
            Timber.e("Stop : %s", idReveil.toString())
            viewModelReveil.stopAlarm(idReveil)
            this.finish()
        }
    }

    override fun finish() {
        super.finish()
        threadVolumeProgressif?.interrupt()
        threadNoMusic?.interrupt()
    }

    private fun snooze() {
        val idReveil = intent!!.getIntExtra(EXTRA_ID, -1)
        if (idReveil != -1) {
            viewModelReveil.snoozeAlarm(idReveil)
            Utility.createSimpleToast(Alarm.getTextNextClock(System.currentTimeMillis() + (Alarm.DUREE_SNOOZE * 60 * 1000)))
            this.finish()
            // TODO annuler le snooze via une icone
        }
    }

    private fun prepareSong(ringing: Ringing) {
        val song = ringing.song!!

        if (youTubePlayer != null) {
            youTubePlayer!!.loadVideo(song.id, 0f) // todo ajout lancement d??call?? ?
            youTubePlayerView.getPlayerUiController()
                .setVideoTitle(song.title)
        }
        viewModelSonnerie.utilisationSonnerie(ringing)
    }

    override fun onBackPressed() {
        stop()
    }
}
