package com.wakemeup.song

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.wakemeup.AppWakeUp
import com.wakemeup.MainActivity
import com.wakemeup.R
import com.wakemeup.util.loadFavoris
import com.wakemeup.util.persisteFavoris
import com.wakemeup.util.resetFavoris
import kotlinx.android.synthetic.main.fragment_musiques_attente.view.*

class MusiquesAttenteFragment : Fragment() {

    private lateinit var dialogue : DialogueYoutube
    private var isPlaying: Boolean = false
    private val songList = mutableListOf<Song>()
    private var favorisListe : MutableList<Song> = mutableListOf<Song>()

    private lateinit var mAdapter: SongAdapter
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var currentView: View

    private var currentIndex: Int = 0
    private var currentSongLength: Int = 0
    private var firstLaunch = true
    private var currentSong: Song? = null

    private var youTubePlayer: YouTubePlayer? = null




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
/*
    private fun gestionFavoris(){
        //Gestion de la persistance des favoris (sauvgarde les favoris dans un fichier)
        if (loadFavoris(this.requireContext()) != null) {
            favorisListe = loadFavoris(this.requireContext())!!
        }
        else{
            favorisListe = mutableListOf<Song>()
        }
        favorisListe.add(currentSong!!)
        resetFavoris(this.requireContext())
        persisteFavoris(this.requireContext(),favorisListe)
    }*/



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        currentView = inflater.inflate(R.layout.fragment_musiques_attente, container, false)

        val recyclerView = currentView.recycler_list_video_musiques_en_attente
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter


        youTubePlayerView = currentView.youtube_player_view_musiques_attente
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


//        textePasDeFavori = currentView.findViewById(R.id.texte_pas_de_favori)

        //Charge les musiques en attente -----------------------------------------------------
        val musiques : MutableList<Song>? = null //todo AppWakeUp.listSonneriesEnAttente
        if (musiques != null) {
            songList.addAll(musiques)
            if (musiques.isEmpty()) {
                currentView.texte_pas_de_musiques_en_attente.visibility = View.VISIBLE
            }
            else{
                currentView.texte_pas_de_musiques_en_attente.visibility=View.INVISIBLE
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
            currentView.texte_pas_de_musiques_en_attente.visibility = View.VISIBLE
        }
        //------------------------------------------------------------------------





        mAdapter.notifyDataSetChanged()
        mAdapter.selectedPosition = 0

        dialogue = DialogueYoutube(activity!!)

        return currentView
    }

    companion object {

        fun newInstance(ctx: Context): MusiquesAttenteFragment {

            val nf = MusiquesAttenteFragment()
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