package com.wakemeup.song

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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

    private lateinit var dialogue : DialogueYoutube
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
    private lateinit var textePasDeFavori : TextView


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
                if (songList.isNullOrEmpty()){
                    currentSong = null
                    textePasDeFavori.visibility=View.VISIBLE
                }

                //Mise à jour-----------------

                mAdapter.notifyDataSetChanged()
                mAdapter.selectedPosition = 0
                //------------------------------

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

    //Gestion du clic sur le bouton partage
    private fun gestionBoutonParatage(){
        val btPartage = currentView.findViewById<Button>(R.id.button_partage_favori)
        btPartage.setOnClickListener {
            if (AppWakeUp.auth.currentUser!!.isAnonymous) {
                dialogue.createAlertDialogNotConnected(context!!, this.activity!! as MainActivity)
            } else {
                if (currentSong != null) {
                    dialogue.createDialoguePartage(currentSong) //lance la dialogue pour preciser le temps
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
        gestionBoutonParatage()

        textePasDeFavori = currentView.findViewById(R.id.texte_pas_de_favori)

        //Charge les favoris-----------------------------------------------------
        val favoris : MutableList<Song>? = loadFavoris(this.requireContext())
        if (favoris != null) {
            songList.addAll(favoris)
            if (favoris.isEmpty()) {
                textePasDeFavori.visibility = View.VISIBLE
            }
            else{
                textePasDeFavori.visibility=View.INVISIBLE
            }

            //Lancer la 1ere video youtube de la liste
            if (songList.isNotEmpty()) {
                currentSong = songList[0]
                changeSelectedSong(0)
                prepareSong(currentSong)
            }
            //----------------------------------------

        }
        else{
            currentSong = null
            textePasDeFavori = currentView.findViewById<Button>(R.id.texte_pas_de_favori)
            textePasDeFavori.visibility=View.VISIBLE
        }
        //------------------------------------------------------------------------





        mAdapter.notifyDataSetChanged()
        mAdapter.selectedPosition = 0

        dialogue = DialogueYoutube(activity!!)

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


