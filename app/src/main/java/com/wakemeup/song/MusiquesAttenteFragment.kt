package com.wakemeup.song

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.neocampus.repo.ViewModelFactory
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.wakemeup.AppWakeUp
import com.wakemeup.MainActivity
import com.wakemeup.R
import com.wakemeup.contact.SonnerieRecue
import com.wakemeup.util.loadFavoris
import com.wakemeup.util.persisteFavoris
import com.wakemeup.util.resetFavoris
import kotlinx.android.synthetic.main.activity_reveil_edit.*
import kotlinx.android.synthetic.main.fragment_musiques_attente.view.*
import java.util.*

class MusiquesAttenteFragment : Fragment(), SonnerieAdapter.RecyclerItemClickListener {

    private lateinit var dialogue : DialogueYoutube
    private var isPlaying: Boolean = false
    private val sonnerieAttenteMap = mutableMapOf<String, SonnerieRecue>()

    private lateinit var mAdapter: SonnerieAdapter
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var currentView: View

    private var currentIndex: Int = 0
    private var currentSongLength: Int = 0
    private var firstLaunch = true
    private var currentSong: Song? = null

    private var youTubePlayer: YouTubePlayer? = null

    private lateinit var viewModel: MusiquesListesViewModel


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val factory = ViewModelFactory(AppWakeUp.repository)
        viewModel = ViewModelProvider(this, factory).get(MusiquesListesViewModel::class.java)
        viewModel.getListeAttenteLiveData().observe(viewLifecycleOwner, Observer { list ->
            updateAttenteListe(list)
        })
    }

    private fun updateAttenteListe(nouvelleListeSonneries: Map<String, SonnerieRecue>) {
        Log.e("Error", "updateAttenteListe")
        sonnerieAttenteMap.clear()
        sonnerieAttenteMap.putAll(nouvelleListeSonneries)
        mAdapter.notifyDataSetChanged()
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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        currentView = inflater.inflate(R.layout.fragment_musiques_attente, container, false)

        mAdapter =  SonnerieAdapter(this.requireContext(), sonnerieAttenteMap,this)
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
        sonnerieAttenteMap.clear()


        if (sonnerieAttenteMap.isEmpty()) {
            currentView.texte_pas_de_musiques_en_attente.visibility = View.VISIBLE
        }
        else{
            currentView.texte_pas_de_musiques_en_attente.visibility=View.INVISIBLE
        }
        //------------------------------------------------------------------------





        mAdapter.notifyDataSetChanged()
        mAdapter.selectedPosition = 0

        dialogue = DialogueYoutube(requireActivity())

        return currentView
    }

    companion object {

        fun newInstance(): MusiquesAttenteFragment {

            val nf = MusiquesAttenteFragment()
            return nf
        }
    }

    override fun onClickSonnerieListener(sonnerie: SonnerieRecue, position: Int) {
        changeSelectedSong(position)
        prepareSong(sonnerie.song)
    }
}