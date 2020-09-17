package com.vguivarc.wakemeup.reveil

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.PowerManager
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.repo.ViewModelFactory
import com.vguivarc.wakemeup.sonnerie.Sonnerie
import com.vguivarc.wakemeup.sonnerie.SonnerieListeViewModel
import com.vguivarc.wakemeup.util.Utility
import kotlinx.android.synthetic.main.activity_reveil_sonne.*
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Reveil sonne activity
 *
 * @constructor Create empty Reveil sonne activity
 */
class ReveilSonneActivity : AppCompatActivity() {

    private var mediaPlayer : MediaPlayer? = null
    private val loading = MutableLiveData<Boolean>()

    private lateinit var audioManager: AudioManager
    private var initialVolume = 0

    /**
     * Sonnerie alarm worker
     *
     * @property context
     * @constructor
     *
     * @param params
     */
    class SonnerieAlarmWorker(val context: Context, params: WorkerParameters) : Worker(context, params) {
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
    }
/*
    class SonnerieVolumeAlarmWorker(val context: Context, params: WorkerParameters) : Worker(context, params) {
        override fun doWork(): Result {
            Timber.e("lol")
            return Result.success()
        }
    }*/

    private var requestVolume : PeriodicWorkRequest?= null

    /**
     * Volume progressif
     *
     */
    fun volumeProgressif(){
       /* for(progressionVolume in 0..10){
            val newVol = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) - audioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC) / 10) * progressionVolume
            } else {
                (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 10) * progressionVolume
            }
            Timber.e("volume=$newVol")
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC, // Stream type
                newVol, // Volume index
                AudioManager.FLAG_SHOW_UI// Flags
            )
            Thread.sleep(3_000)
        }*/
        audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    }
/*    fun setProgressifVolume(){
        Timber.e("setProgressifVolume")
        requestVolume = PeriodicWorkRequestBuilder<SonnerieVolumeAlarmWorker>(1, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(this)
            .enqueue(requestVolume!!)

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(requestVolume!!.id).observe(this, androidx.lifecycle.Observer {workStatus->
            Timber.e("getWorkInfoByIdLiveData"+workStatus.toString())
            if (workStatus != null) {
                Timber.e("progressionVolume++")
                    progressionVolume++
                    if(progressionVolume<=10) {
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
                    }
            }
        })
    }

*/

    private var youTubePlayer: YouTubePlayer? = null
    private lateinit var youTubePlayerView: YouTubePlayerView
    var currentSonnerie : Sonnerie? = null

    private lateinit var viewModelSonnerie : SonnerieListeViewModel
    private lateinit var loadingView : ProgressBar
    private lateinit var senderView : TextView
    private lateinit var viewModelReveil : ReveilListeViewModel
    private lateinit var textPasDeMusiqueAttente : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reveil_sonne)
        youTubePlayerView = findViewById(R.id.youtubePlayReveil)
        loadingView = findViewById(R.id.id_loading_sonnerie)
        senderView = findViewById(R.id.sender_sonnerie)
        textPasDeMusiqueAttente = findViewById(R.id.text_pas_de_musique_attente)
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        initialVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        loading.observe(this, androidx.lifecycle.Observer {
            if(it){
                loadingView.visibility = View.VISIBLE
            } else {
                loadingView.visibility = View.GONE
            }
        })
        loading.value=true
        youTubePlayerView.visibility = View.INVISIBLE

        val request = OneTimeWorkRequest.Builder(SonnerieAlarmWorker::class.java)
            .setInitialDelay(10000, TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(AppWakeUp.appContext)
            .beginUniqueWork("sonnerieParDefaut", ExistingWorkPolicy.REPLACE, request)
            .enqueue()


        WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.id).observe(this, androidx.lifecycle.Observer {workStatus->
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
        })

        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)

        val powermanager =
            this.getSystemService(Context.POWER_SERVICE) as PowerManager

        val wakeLock = powermanager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "musicme:tag"
        )

     /*   val wakeLock = powermanager.newWakeLock(
            PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "musicme:tag"
        )*/
        wakeLock.acquire(5 * 60 * 1000L /*10 minutes*/)

        if (wakeLock.isHeld) {
            wakeLock.release()
        }

        val factory = ViewModelFactory(AppWakeUp.repository)
        viewModelSonnerie = ViewModelProvider(this, factory).get(SonnerieListeViewModel::class.java)
        viewModelReveil = ViewModelProvider(this, factory).get(ReveilListeViewModel::class.java)

      //  viewModelSonnerie.updateSonneries()




        viewModelSonnerie.getListeAttenteLiveData().observe(this, androidx.lifecycle.Observer {
            if (it.isEmpty()) {
                // pas de musique
            } else {
                currentSonnerie = it.values.toMutableList()[0]
                //Init du youTubePlayerView-----------------------------------------------------------------------
                lifecycle.addObserver(youTubePlayerView)
                Timber.e("ytPlayer")
                senderView.text=this.getString(R.string.envoyee_par, currentSonnerie!!.senderName)

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
                        Timber.e("Error YT %s", currentSonnerie)
                        Timber.e("Error YT %s", currentSonnerie!!.idSong)

                        //TODO musique par défaut -> marche pas, error se lance pas si pas internet

                    }

                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        this@ReveilSonneActivity.youTubePlayer = youTubePlayer
                        Timber.e("Ready musique")
                        prepareSong(currentSonnerie!!)
                        youTubePlayerView.visibility=View.VISIBLE
                        loading.value = false
                        WorkManager.getInstance(AppWakeUp.appContext).cancelAllWork()
                        volumeProgressif()
                    }
                })
                //-------------------------------------------------------------------------------------------------

            }
        })

        //Initialisation des valeures de la date et heure----
        val calendar : Calendar = Calendar.getInstance()

        val jour = calendar.get(Calendar.DAY_OF_MONTH)
        val moisTxt = when(calendar.get(Calendar.MONTH) +1){
            1 -> "janvier"
            2 -> "février"
            3 -> "mars"
            4 -> "avril"
            5 -> "mai"
            6 -> "juin"
            7 -> "juillet"
            8 -> "août"
            9 -> "septembre"
            10 -> "octobre"
            11 -> "novembre"
            else -> "décembre"
        }
        val annee = calendar.get(Calendar.YEAR)

        val heure = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        date_sonnerie.text = "${jour} / ${moisTxt} / ${annee}"
        if (minute<10)
            heure_sonnerie.text = "$heure : 0$minute"
        else
            heure_sonnerie.text = "$heure : $minute"
        //-----------------------------------------------------

        //Gestion du bouton stop--------------------------------------
        bouton_reveil_stop.setOnClickListener{
            stop()
        }
        //------------------------------------------------------------

        //Gestion du bouton snooze--------------------------------------------------------------
        bouton_reveil_snooze.setOnClickListener {
            snooze()
        }
        //--------------------------------------------------------------------------------------

        //TODO pas de texte de snooze car lié à une activité qui s'arrête juste après
        //--------------------------------------------------------------------------------------

    }

    private fun stop() {
        WorkManager.getInstance(this).getWorkInfoById(requestVolume!!.id).cancel(true)
        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC, // Stream type
            initialVolume, // Volume index
            AudioManager.FLAG_SHOW_UI// Flags
        )

        if(mediaPlayer!=null){
            mediaPlayer!!.stop()
        }
        val idReveil = intent!!.getIntExtra("idReveil", -1)
        if(idReveil!=-1)
        {
            Timber.e("Stop : %s", idReveil.toString())
            viewModelReveil.stopReveil(idReveil)
            this.finish()
        }
    }

    private fun snooze(){
        val idReveil = intent!!.getIntExtra("idReveil", -1)
        if(idReveil!=-1)
        {
            viewModelReveil.snoozeReveil(idReveil)
            Utility.createSimpleToast(ReveilModel.getTextNextClock(System.currentTimeMillis() + (ReveilModel.DUREE_SNOOZE *60*1000)))
            this.finish()
            //TODO annuler le snooze via une icone
        }
    }

    private fun prepareSong(sonnerie: Sonnerie) {
        val song = sonnerie.song!!

                if (youTubePlayer != null) {
                    youTubePlayer!!.loadVideo(song.id,0f)//todo ajout lancement décallé ?
                    youTubePlayer!!.setVolume(100)
                    youTubePlayerView.getPlayerUiController()
                        .setVideoTitle(song.title)
                }
                viewModelSonnerie.utilisationSonnerie(sonnerie)

    }

    override fun onBackPressed() {
        stop()
    }
}
