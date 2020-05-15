package com.wakemeup.song

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.neocampus.repo.ViewModelFactory
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.wakemeup.AppWakeUp
import com.wakemeup.R
import com.wakemeup.contact.SonnerieRecue
import kotlinx.android.synthetic.main.fragment_musiques_attente.view.*

class MusiquesAttenteFragment : Fragment(), SonnerieAdapter.RecyclerItemClickListener {

    private val sonnerieAttenteList = mutableListOf<SonnerieRecue>()

    private lateinit var mAdapter: SonnerieAdapter
    private lateinit var currentView: View

    private var currentIndex: Int = 0
    private var currentSongLength: Int = 0
    private var currentSong: Song? = null

    private var youTubePlayer: YouTubePlayer? = null

    private lateinit var viewModel: MusiquesListesViewModel


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mAdapter =  SonnerieAdapter(this.requireContext(), sonnerieAttenteList,this)
        val recyclerView = currentView.recycler_list_video_musiques_en_attente
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter

        val factory = ViewModelFactory(AppWakeUp.repository)
        viewModel = ViewModelProvider(requireActivity()).get(MusiquesListesViewModel::class.java)
        viewModel.getListeAttenteLiveData().observe(viewLifecycleOwner, Observer { list ->
            updateAttenteListe(list)
        })
    }

    private fun updateAttenteListe(state: MusiquesListViewState) {
        Log.e("update", "updateAttenteListe = " + state.musiques)
        if (state.hasMusiquesChanged){
            sonnerieAttenteList.clear()
            sonnerieAttenteList.addAll(state.musiques.values)
            //todo tri par date
            sonnerieAttenteList.sortWith(compareBy { v -> v.sonnerieId })
            mAdapter.notifyDataSetChanged()
            currentView.texte_pas_de_musiques_en_attente.visibility = INVISIBLE
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        currentView = inflater.inflate(R.layout.fragment_musiques_attente, container, false)


        //Initialisation du recyclerView (Le principal, pour les vidéos youtube)----------------------------
      /*  mAdapter =  SongAdapter(
            this.requireContext(),
            songList,
            object : SongAdapter.RecyclerItemClickListener {
                override fun onClickListener(song: Song, position: Int) {
                    changeSelectedSong(position)
                    prepareSong(song)
                }
            })*/
        mAdapter =  SonnerieAdapter(this.requireContext(), sonnerieAttenteMap,this)
        val recyclerView = currentView.recycler_list_video_musiques_en_attente
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter
        //---------------------------------------------------------------------------------------------------


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

        mAdapter.notifyDataSetChanged()
        mAdapter.selectedPosition = 0

        dialogue = DialogueYoutube(requireActivity())

        return currentView
    }

    companion object {

        fun newInstance(ctx: Context): MusiquesAttenteFragment {

            val nf = MusiquesAttenteFragment()
            return nf
        }
        fun newInstance(): MusiquesAttenteFragment {
            return MusiquesAttenteFragment()
        }
    }

    override fun onClickSonnerieListener(sonnerie: SonnerieRecue, position: Int) {

        //todo ouverture d'une fenetre d'options ?
    }
}