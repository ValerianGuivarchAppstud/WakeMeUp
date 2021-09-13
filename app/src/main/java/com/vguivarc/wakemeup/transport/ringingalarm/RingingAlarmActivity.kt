package com.vguivarc.wakemeup.transport.ringingalarm

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.*
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.vguivarc.wakemeup.AndroidApplication
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.databinding.ActivityReveilSonneBinding
import com.vguivarc.wakemeup.domain.internal.AlarmAndroidProvider.Companion.EXTRA_ID
import kotlinx.android.synthetic.main.activity_reveil_sonne.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.orbitmvi.orbit.viewmodel.observe
import timber.log.Timber
import java.util.*


class RingingAlarmActivity : AppCompatActivity(R.layout.activity_reveil_sonne) {

    private val viewModel by viewModel<RingingAlarmViewModel>()

    private var _binding: ActivityReveilSonneBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var mediaPlayer: MediaPlayer? = null
    private var youtubePlayer: YouTubePlayer? = null

    private lateinit var audioManager: AudioManager

    private var threadVolumeProgressif: Thread? = null
    private var threadNoMusic: Thread? = null

/*    private fun dealWithNoMusic(): Thread {
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
            }
        }

        val workerDealWithNoMusic = WorkerDealWithNoMusic(handler)
        val thread = Thread(workerDealWithNoMusic)
        thread.start()
        return thread
    }
*/
/*    private fun dealWithVolume(): Thread {
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
                //youTubePlayer!!.setVolume(msg.arg1)
            }
        }

        val workerDealWithVolume = WorkerDealWithVolume(handler)
        val thread = Thread(workerDealWithVolume)
        thread.start()
        return thread
    }
*/
//    private var youTubePlayer: YouTubePlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityReveilSonneBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_reveil_sonne)

        viewModel.observe(this, state = ::render, sideEffect = ::handleSideEffect)


        val idReveil = intent!!.getIntExtra(EXTRA_ID, -1)
        viewModel.getAlarm(idReveil)

//        _binding = ActivityReveilSonneBinding.inflate(layoutInflater)

        lifecycle.addObserver(binding.youtubePlayerView)



        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
//        initialVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        binding.youtubePlayerView.visibility = View.INVISIBLE
        binding.boutonReveilStop.setOnClickListener {
            viewModel.stopAlarm()
        }
        binding.boutonReveilSnooze.setOnClickListener {
            viewModel.snoozeAlarm()
        }


     /*   (old)val request = OneTimeWorkRequest.Builder(SonnerieAlarmWorker::class.java)
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

        ///////threadNoMusic = dealWithNoMusic()

   /*     viewModelSonnerie.getListeAttenteLiveData().observe(
            this,
            {
                if (it.isEmpty()) {
                    // pas de musique, ou pas encore chargé donc on fait rien
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
                                // TODO musique par défaut -> marche pas, error se lance pas si pas internet
                            }

                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                this@RingingAlarmActivity.youTubePlayer = youTubePlayer
                                Timber.e("Ready musique")
                                prepareSong(currentRinging!!)
                                youTubePlayerView.visibility = View.VISIBLE
                                loading.value = false
                                // WorkManager.getInstance(AppWakeUp.appContext).cancelAllWork()
                                threadNoMusic?.interrupt()
//////                                threadVolumeProgressif = dealWithVolume()
                            }
                        })
                    // -------------------------------------------------------------------------------------------------
                }
            }
        )*/

        // Initialisation des valeures de la date et heure----
        val calendar: Calendar = Calendar.getInstance()

        val jour = calendar.get(Calendar.DAY_OF_MONTH)
        val moisTxt = when (calendar.get(Calendar.MONTH) + 1) {
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

        date_sonnerie.text = "$jour $moisTxt $annee"
        if (minute <10)
            heure_sonnerie.text = "$heure : 0$minute"
        else
            heure_sonnerie.text = "$heure : $minute"
        // -----------------------------------------------------

        // TODO pas de texte de snooze car lié à une activité qui s'arrête juste après
        // --------------------------------------------------------------------------------------
    }



    private fun render(state: RingingAlarmState) {
        Timber.d("render $state")
        when(state.step){
            RingingAlarmStep.WaitingForNextRinging -> {
                binding.loader.isVisible = true
            }
            RingingAlarmStep.WaitingForYoutubeReader -> {
                state.ringing?.let {
                    binding.senderSonnerie.text = "Musique envoyée par ${it.senderName}"
                    binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(yt: YouTubePlayer) {
                            youtubePlayer = yt
                            viewModel.youtubePlayerReady()
                        }
                    })
                }?:run {
                    viewModel.errorPlayYoutubeSong()
                }
            }
            RingingAlarmStep.ReadyToPlay -> {
                youtubePlayer?.let { youtubePlayer ->
                    state.ringing?.let { ringing ->
                        youtubePlayer.loadVideo(ringing.song!!.id, 0f)
                    }
                }?:run {
                    viewModel.errorPlayYoutubeSong()
                }
                binding.youtubePlayerView.isVisible = true
                binding.loader.isVisible = false
                viewModel.isPlaying()
            }
            RingingAlarmStep.Playing -> {
                youtubePlayer?.let {
                    it.setVolume(state.volume)
                }?:run {
                    viewModel.errorPlayYoutubeSong()
                }
            }
            RingingAlarmStep.NoNextRinging -> {
                binding.senderSonnerie.text = "Pas de sonnerie en attente"
                binding.loader.isVisible = false
                playLocalMusic()
            }
        }
/*        state.ringing?.let {
            if(!state.isPlaying){
                viewModel.play()
                youTubePlayer?.loadVideo(state.ringing.song!!.id, 0f)
                binding.youtubePlayerView.getPlayerUiController()
                    .setVideoTitle(state.ringing.song!!.title)

            }
        }*/
    }

    private fun playLocalMusic() {
        val resID = AndroidApplication.appContext.resources.getIdentifier("sonnerie_default", "raw", AndroidApplication.appContext.packageName)
        mediaPlayer = MediaPlayer.create(AndroidApplication.appContext, resID)
         audioManager.setStreamVolume(
             AudioManager.STREAM_MUSIC, // Stream type
             audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), // Volume index
             AudioManager.FLAG_SHOW_UI// Flags
         )
        mediaPlayer!!.start()
    }

    private fun handleSideEffect(sideEffect: RingingAlarmSideEffect) {
        when (sideEffect) {
            is RingingAlarmSideEffect.Toast -> Toast.makeText(
                this,
                sideEffect.textResource,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
/*
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
            youTubePlayer!!.loadVideo(song.id, 0f) // todo ajout lancement décallé ?
            youTubePlayerView.getPlayerUiController()
                .setVideoTitle(song.title)
        }
        viewModelSonnerie.utilisationSonnerie(ringing)
    }
*/
    override fun onBackPressed() {
        viewModel.stopAlarm()
    }
}
