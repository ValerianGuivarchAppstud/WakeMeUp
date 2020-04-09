package com.wakemeup.alarmclock

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.wakemeup.R
import com.wakemeup.song.Song


class ReveilSonneActivity : AppCompatActivity() {
    private val TAG: String = "ReveilSonneActivity"

    private var youTubePlayer: YouTubePlayer? = null
    private lateinit var youTubePlayerView: YouTubePlayerView


    @SuppressLint("InvalidWakeLockTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reveil_sonne)

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

        //musique test
        currentSong = Song("fHI8X4OXluQ", "Blinding the light", "The weekdn", "test", 100)

        youTubePlayerView = findViewById(R.id.youtubePlayReveil)
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
                super.onError(youTubePlayer, error)
                //TODO musique par défaut -> marche pas, error se lance pas si pas internet
                Log.e(TAG, "erreur")
                Log.e(TAG, error.name)
                Log.e(TAG, error.toString())
            }

            override fun onReady(youTubePlayer: YouTubePlayer) {
                Log.e(TAG, "ready")
                this@ReveilSonneActivity.youTubePlayer = youTubePlayer
                if (currentSong != null) {
                    prepareSong(currentSong)
                }
            }
        })
    }

    private var currentSong: Song? = null
    private var currentSongLength: Int = 0
    private fun prepareSong(song: Song?) {
        if (song != null) {
            currentSongLength = song.duration
            currentSong = song

            if (youTubePlayer != null) {
                youTubePlayer!!.loadVideo(song.id, 0F)
                youTubePlayer!!.setVolume(100)
                youTubePlayerView.getPlayerUiController()
                    .setVideoTitle(song.title)//.loadVideo(videoId, 0F)
            }
        }
    }
}
