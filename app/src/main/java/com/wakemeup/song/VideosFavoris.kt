package com.wakemeup.song

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.api.services.youtube.model.SearchResult
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.wakemeup.AppWakeUp
import com.wakemeup.MainActivity
import com.wakemeup.R
import com.wakemeup.contact.ListFriendToSendMusicActivity
import com.wakemeup.util.loadFavoris
import com.wakemeup.util.persisteFavoris
import com.wakemeup.util.resetFavoris
import kotlinx.android.synthetic.main.fragment_video.*
import kotlinx.android.synthetic.main.fragment_video.view.*


class VideosFavoris : Fragment() {

    private var isPlaying: Boolean = false
    private val songList : MutableList<Song> = mutableListOf<Song>()

    private lateinit var mAdapter: SongAdapter
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var currentView: View

    private var currentIndex: Int = 0
    private var currentSongLength: Int = 0
    private var firstLaunch = true
    private var currentSong: Song? = null

    private var youTubePlayer: YouTubePlayer? = null


    private fun createAlertDialogNotConnected(context: Context, ma: MainActivity) {
        // Initialize a new instance of
        val builder = AlertDialog.Builder(context)

        // Set the alert dialog title
        builder.setTitle("Vous n'êtes pas connecté")

        // Display a message on alert dialog
        builder.setMessage("Pour partager une musique à un contact, veuillez vous connecter.")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Se connecter") { dialog, which ->
            ma.startConnectActivity(false)
        }
        // Display a neutral button on alert dialog
        builder.setNeutralButton("Annuler") { _, _ -> }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }


    private fun changeSelectedSong(index: Int) {
        mAdapter.notifyItemChanged(mAdapter.selectedPosition)
        currentIndex = index
        mAdapter.selectedPosition = currentIndex
        mAdapter.notifyItemChanged(currentIndex)
    }



    //lance la video reliée au parametre "song"
    private fun prepareSong(song: Song?) {
        //todo vérifier bug ici, si on sélectionne la musique trop vite
        if (song != null) {

            currentSongLength = song.duration
            currentSong = song

            if (youTubePlayer != null) {
                youTubePlayer!!.loadVideo(song.id, 0F)
                //////// todo youTubePlayer.setVolume(100)
                youTubePlayerView.getPlayerUiController()
                    .setVideoTitle(song.title)//.loadVideo(videoId, 0F)

                this.isPlaying = true
            }
        }
    }

    //TODO créer classe abstraite ou créer classe BouttonPartage

    //Gestion du clic sur le bouton suprimer
    private fun gestionBoutonSupprimer(){
        val btSupprimer = currentView.findViewById<Button>(R.id.bouton_supprimer_favori)
        btSupprimer.setOnClickListener {

            if (currentSong != null) {
                songList.remove(currentSong!!)
                mAdapter.notifyDataSetChanged()
                mAdapter.selectedPosition = 0
                resetFavoris(this.requireContext())
                persisteFavoris(this.requireContext(),songList)

                Toast.makeText(
                    activity!!.application,
                    "Vidéo Suprimmée",
                    Toast.LENGTH_SHORT
                ).show()

            }
            else{
                Toast.makeText(
                    activity!!.application,
                    "Veuillez sélectionner une vidéo",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        currentView = inflater.inflate(R.layout.fragment_favori, container, false)

        val recyclerView = currentView.findViewById<RecyclerView>(R.id.recycler_list_video_favoris)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter


        youTubePlayerView = currentView.findViewById(R.id.youtube_player_view_favoris)
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(ytPlayer: YouTubePlayer) {
                youTubePlayer = ytPlayer
                if (currentSong != null) {
                    prepareSong(currentSong)
                }
            }
        })

        currentIndex = 0
        currentView.pb_main_loader.visibility = View.GONE
        songList.clear()

        gestionBoutonSupprimer()

        //Charge les favoris-----------------------------------------------------
        val favoris : MutableList<Song>? = loadFavoris(this.requireContext())
        if (favoris != null) {
            songList.addAll(favoris)
        }
        //------------------------------------------------------------------------


        mAdapter.notifyDataSetChanged()
        mAdapter.selectedPosition = 0

        return currentView
    }

    companion object {

        fun newInstance(ctx: Context): VideosFavoris {

            val nf = VideosFavoris()
            nf.mAdapter = SongAdapter(ctx, nf.songList,
                object : SongAdapter.RecyclerItemClickListener {
                    override fun onClickListener(song: Song, position: Int) {
                        nf.firstLaunch = false
                        nf.changeSelectedSong(position)
                        nf.prepareSong(song)
                    }
                })
            return nf
        }
    }


}


