package com.wakemeup.reveil

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.wakemeup.AppWakeUp
import com.wakemeup.MainActivity
import com.wakemeup.R
import com.wakemeup.song.DialogueYoutube
import com.wakemeup.song.Song
import com.wakemeup.song.SongHistorique
import com.wakemeup.song.SongIndex
import com.wakemeup.util.loadFavoris
import com.wakemeup.util.persisteFavoris
import com.wakemeup.util.resetFavoris
import kotlinx.android.synthetic.main.activity_reveil_sonne.*
import kotlinx.android.synthetic.main.fragment_musiques_passees.*
import java.util.*


class ReveilSonneActivity : AppCompatActivity() {

    //TODO POUVOIR ACTIVER L'ALARM QUAND L'APPLICATION EST DESACTIVER

    private var youTubePlayer: YouTubePlayer? = null
    private lateinit var youTubePlayerView: YouTubePlayerView
    private var favorisListe = SongIndex()
    lateinit var currentSong : Song

    private fun gestionFavoris(){
        //Gestion de la persistance des favoris (sauvgarde les favoris dans un fichier)
        var index : Int
        val listeTemp = loadFavoris(this)
        if (listeTemp!=null) {
            if (listeTemp.index !=0) {
                favorisListe = listeTemp
            }
        }

        favorisListe.list.add(SongHistorique(favorisListe.index,currentSong!!))
        favorisListe.index++
        resetFavoris(this)
        persisteFavoris(this,favorisListe)
    }




    @SuppressLint("InvalidWakeLockTag")
    override fun onCreate(savedInstanceState: Bundle?) {

        //musique test
        currentSong = Song("fHI8X4OXluQ", "Blinding the light", "The weekdn", "test",0, 0)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reveil_sonne)

        //Initialisation de la liste des favoris-------------------------------------------------
        val chargement_des_favoris = loadFavoris(this)
        if (chargement_des_favoris != null) {
            favorisListe = chargement_des_favoris
        }
        //---------------------------------------------------------------------------------------

        //Initialisation des valeures de la date et heure----
        val calendar : Calendar = Calendar.getInstance()

        val jour = calendar.get(Calendar.DAY_OF_MONTH)
        val mois = calendar.get(Calendar.MONTH) +1
        val anne = calendar.get(Calendar.YEAR)

        val heure = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        date_sonnerie.text = "${jour} / ${mois} / ${anne}"
        if (minute<10)
            heure_sonnerie.text = "$heure : 0$minute"
        else
            heure_sonnerie.text = "$heure : $minute"
        //-----------------------------------------------------

        //Gestion du bouton stop--------------------------------------
        bouton_reveil_stop.setOnClickListener{
            this.finish()
        }
        //------------------------------------------------------------

        //Gestion du bouton snooze--------------------------------------------------------------
        bouton_reveil_snooze.setOnClickListener {
            //TODO (demander à thomas pour la fonction)
        }
        //--------------------------------------------------------------------------------------

        //Gestion du bouton favori--------------------------------------------------------------
        button_favoris_reveil_sonne.setOnClickListener {
            val dialogue = DialogueYoutube(this)
            if (AppWakeUp.auth.currentUser!!.isAnonymous) {
                dialogue.createAlertDialogNotConnected(this, this as MainActivity)
            } else {
                if (currentSong!=null) {
                    gestionFavoris()
                }
                Toast.makeText(
                    application,
                    "La video a été ajouté",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        //--------------------------------------------------------------------------------------

        val powermanager =
            this.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powermanager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "tag"
        )
        wakeLock.acquire(5 * 60 * 1000L /*10 minutes*/)

        if (wakeLock.isHeld) {
            wakeLock.release()
        }


        //Init du youTubePlayerView-----------------------------------------------------------------------
        youTubePlayerView = findViewById(R.id.youtubePlayReveil)
        lifecycle.addObserver(youTubePlayerView)


        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
                super.onError(youTubePlayer, error)
                //TODO musique par défaut -> marche pas, error se lance pas si pas internet

            }

            override fun onReady(youTubePlayer: YouTubePlayer) {
                this@ReveilSonneActivity.youTubePlayer = youTubePlayer
                if (currentSong != null) {
                    prepareSong(currentSong)
                }
            }
        })
        //-------------------------------------------------------------------------------------------------

    }

    private var currentSongLength: Int = 0

    private fun prepareSong(song: Song?) {
        //todo vérifier bug ici, si on sélectionne la musique trop vite
        if (song != null) {
            currentSongLength = song.duration
            currentSong = song

            if (youTubePlayer != null) {
                youTubePlayer!!.loadVideo(song.id,song.lancement.toFloat())
                youTubePlayer!!.setVolume(100)
                youTubePlayerView.getPlayerUiController()
                    .setVideoTitle(song.title)
            }
        }
    }
}
