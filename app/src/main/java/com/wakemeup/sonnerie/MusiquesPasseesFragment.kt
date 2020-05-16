package com.wakemeup.sonnerie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wakemeup.R
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.wakemeup.AppWakeUp
import com.wakemeup.MainActivity
import com.wakemeup.song.DialogueYoutube
import com.wakemeup.song.SongHistorique
import com.wakemeup.song.SongIndex
import com.wakemeup.util.loadFavoris
import com.wakemeup.util.persisteFavoris
import com.wakemeup.util.resetFavoris
import kotlinx.android.synthetic.main.fragment_musiques_passees.view.*

class MusiquesPasseesFragment : Fragment(), SonnerieAdapter.RecyclerItemClickListener {

    private lateinit var dialogue : DialogueYoutube
    private var isPlaying: Boolean = false
    private var favorisListe = SongIndex()//: MutableList<Song> = mutableListOf<Song>()

    private lateinit var mAdapter: SonnerieAdapter
//    private lateinit var mAdapter: SongHistoriqueAdaptater
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var currentView: View

    private var currentIndex: Int = 0
    private var currentSongLength: Int = 0
    private var currentSonnerie: SonnerieRecue? = null

    private var youTubePlayer: YouTubePlayer? = null


    private lateinit var viewModel: MusiquesListesViewModel
    private val listMusicPass = mutableListOf<SonnerieRecue>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        val factory = ViewModelFactory(AppWakeUp.repository)
        viewModel = ViewModelProvider(this).get(MusiquesListesViewModel::class.java)
        viewModel.getListePasseesLiveData().observe(
            viewLifecycleOwner,
            Observer { nouvelleListe ->
                updateMusiqueListe(nouvelleListe)
            })
    }

    private fun updateMusiqueListe(state: MusiquesListViewState){
        if (state.hasMusiquesChanged){
            listMusicPass.clear()
            listMusicPass.addAll(state.musiques.values)
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun changeSelectedSong(index: Int) {
        mAdapter.notifyItemChanged(mAdapter.selectedPosition)
        currentIndex = index
        mAdapter.selectedPosition = currentIndex
        mAdapter.notifyItemChanged(currentIndex)
    }

    //lance la video reliée au parametre "song"
    private fun prepareSong(sonnerie: SonnerieRecue?) {
        //todo vérifier bug ici, si on sélectionne la musique trop vite
        if (sonnerie != null) {

            currentSongLength = sonnerie.song.duration
            currentSonnerie = sonnerie

            if (youTubePlayer != null) {
                youTubePlayer!!.loadVideo(sonnerie.song.id, 0F)
                //////// todo youTubePlayer.setVolume(100)
                youTubePlayerView.getPlayerUiController()
                    .setVideoTitle(sonnerie.song.title)//.loadVideo(videoId, 0F)

                this.isPlaying = true
            }
        }
    }

    //TODO créer classe abstraite ou créer classe BouttonPartage


    //Gestion du clic sur le bouton suprimer
    private fun gestionBoutonSupprimer(){
        val btSupprimer = currentView.bouton_supprimer_musiques_passees
        btSupprimer.setOnClickListener {
            if (currentSonnerie != null) {
                if (listMusicPass.isNullOrEmpty()){
                    currentSonnerie = null
                    currentView.texte_pas_de_musiques_passees.visibility = View.VISIBLE
                } else {
                    viewModel.removeSonneriePassee(currentSonnerie!!.sonnerieId, currentSonnerie!!.song.id, requireContext())
                }

                //Mise à jour-----------------

                mAdapter.notifyDataSetChanged()
                mAdapter.selectedPosition = 0
                //------------------------------


                Toast.makeText(
                    requireActivity().application,
                    "Vidéo Suprimmée",
                    Toast.LENGTH_SHORT
                ).show()

            }
            else{
                Toast.makeText(
                    requireActivity().application,
                    "Veuillez sélectionner une vidéo",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    //Gestion du clic sur le bouton favori
    private fun gestionBoutonFavori(){
        val btFavori = currentView.bouton_ajouter_favori
        btFavori.setOnClickListener {
            if (AppWakeUp.auth.currentUser!!.isAnonymous) {
                dialogue.createAlertDialogNotConnected(requireContext(), this.requireActivity() as MainActivity)
            } else {
                if (currentSonnerie!=null) {
                    gestionFavoris()

                    Toast.makeText(
                        requireActivity().application,
                        "La video a été ajouté",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else{
                    Toast.makeText(
                        requireActivity().application,
                        "Veuillez sélectionner une vidéo",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun gestionFavoris(){
        //Gestion de la persistance des favoris (sauvgarde les favoris dans un fichier)
        if (loadFavoris(this.requireContext()) != null) {
            favorisListe = loadFavoris(this.requireContext())!!
        }
        favorisListe.list.add(SongHistorique(favorisListe.index,currentSonnerie!!.song))
        favorisListe.index++
        resetFavoris(this.requireContext())
        persisteFavoris(this.requireContext(),favorisListe)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        currentView = inflater.inflate(R.layout.fragment_musiques_passees, container, false)

        mAdapter = SonnerieAdapter(this.requireContext(), listMusicPass, this)

    /*    val recyclerView = currentView.recycler_list_video_musiques_passees


        //Initialisation du recyclerView (Le principal, pour les vidéos youtube)----------------------------
        mAdapter = SongHistoriqueAdaptater(
            this.requireContext(),
            "MUSIQUESPASSES",
            songList.list,
            object : SongHistoriqueAdaptater.RecyclerItemClickListener {
                override fun onClickListener(songH: SongHistorique, position: Int) {
                    //nfirstLaunch = false
                    changeSelectedSong(position)
                    prepareSong(songH.song)
                }
            })*/
        val recyclerView = currentView.findViewById<RecyclerView>(R.id.recycler_list_video_musiques_passees)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter
        //---------------------------------------------------------------------------------------------------



        youTubePlayerView = currentView.youtube_player_view_musiques_passees
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(ytPlayer: YouTubePlayer) {
                youTubePlayer = ytPlayer
                if (currentSonnerie != null) {
                    prepareSong(currentSonnerie)
                }
            }
        })

        currentIndex = 0
        currentView.pb_main_loader.visibility = View.GONE

        gestionBoutonSupprimer()
        gestionBoutonFavori()





        mAdapter.notifyDataSetChanged()
        mAdapter.selectedPosition = 0

        dialogue = DialogueYoutube(requireActivity())

        return currentView
    }

    companion object {

        fun newInstance(): MusiquesPasseesFragment {

            val nf = MusiquesPasseesFragment()
            //TODO corriger ça ?
            /*
            val songList: MutableList<Song> = mutableListOf()
            for(hs in nf.songList.list){
                songList.add(hs.song)
            }*/
            return nf
        }
    }

    override fun onClickSonnerieListener(sonnerie: SonnerieRecue, position: Int) {
        changeSelectedSong(position)
        prepareSong(sonnerie)
    }

}