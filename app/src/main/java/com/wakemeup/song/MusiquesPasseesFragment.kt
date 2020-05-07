package com.wakemeup.song

import android.content.Context
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
import com.neocampus.repo.ViewModelFactory
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.wakemeup.AppWakeUp
import com.wakemeup.MainActivity
import com.wakemeup.contact.SonnerieRecue
import com.wakemeup.util.loadFavoris
import com.wakemeup.util.persisteFavoris
import com.wakemeup.util.resetFavoris
import kotlinx.android.synthetic.main.activity_liste_ami.*
import kotlinx.android.synthetic.main.fragment_musiques_passees.view.*

class MusiquesPasseesFragment : Fragment(), SongAdapter.RecyclerItemClickListener {

    private lateinit var dialogue : DialogueYoutube
    private var isPlaying: Boolean = false
    private val songList = SongIndex()
    private var favorisListe = SongIndex()

    private lateinit var mAdapter: SongHistoriqueAdaptater
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var currentView: View

    private var currentIndex: Int = 0
    private var currentSongLength: Int = 0
    private var currentSong: Song? = null

    private var youTubePlayer: YouTubePlayer? = null


    private lateinit var viewModel: MusiquesListesViewModel
    private val listMusicPass = mutableMapOf<String, SonnerieRecue>()

    override fun onClickListener(song: Song, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Todo view model
        //val factory = ViewModelFactory(AppWakeUp.repository)
        //viewModel = ViewModelProvider(this, factory).get(MusiquesListesViewModel::class.java)
        /*viewModel.getListePasseesLiveData().observe(
            viewLifecycleOwner,
            Observer { nouvelleListe ->
                //todo songAdapter et viewModel
                // updateMusiqueListe(nouvelleListe)
            })*/
    }

    private fun updateMusiqueListe(nouvelleListe : Map<String, SonnerieRecue>){
        listMusicPass.clear()
        listMusicPass.putAll(nouvelleListe)
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

    //TODO créer classe abstraite ou créer classe BouttonPartage


    //Gestion du clic sur le bouton suprimer
    private fun gestionBoutonSupprimer(){
        val btSupprimer = currentView.bouton_supprimer_musiques_passees
        btSupprimer.setOnClickListener {
            if (currentSong != null) {
                songList.remove(currentSong!!)
                if (songList.list.isNullOrEmpty()){
                    currentSong = null
                    currentView.texte_pas_de_musiques_passees.visibility = View.VISIBLE
                }

                //Mise à jour-----------------

                mAdapter.notifyDataSetChanged()
                mAdapter.selectedPosition = 0
                //------------------------------

                resetFavoris(this.requireContext())
                persisteFavoris(this.requireContext(),songList)

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
                if (currentSong!=null) {
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
        favorisListe.list.add(SongHistorique(favorisListe.index,currentSong!!))
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
            })
        val recyclerView = currentView.findViewById<RecyclerView>(R.id.recycler_list_video_musiques_passees)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter
        //---------------------------------------------------------------------------------------------------



        youTubePlayerView = currentView.youtube_player_view_musiques_passees
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
        songList.list.clear()
        songList.index=0

        gestionBoutonSupprimer()
        gestionBoutonFavori()

//        textePasDeFavori = currentView.findViewById(R.id.texte_pas_de_favori)

        //Charge les musiques en attente -----------------------------------------------------
        val musiques : MutableList<Song>? = null //todo AppWakeUp.listSonneriesEnAttente
        if (musiques != null) {
            for(m in musiques){
                songList.list.add(SongHistorique(favorisListe.index,currentSong!!))
                songList.index++
            }
            if (musiques.isEmpty()) {
                currentView.texte_pas_de_musiques_passees.visibility = View.VISIBLE
            }
            else{
                currentView.texte_pas_de_musiques_passees.visibility=View.INVISIBLE
            }

            //Lancer la 1ere video youtube de la liste
            if (songList.list.isNotEmpty()) {
                currentSong = songList.list[0].song
                changeSelectedSong(0)
                prepareSong(currentSong)
            }
            //----------------------------------------

        }
        else{
            currentSong = null
            currentView.texte_pas_de_musiques_passees.visibility = View.VISIBLE
        }
        //------------------------------------------------------------------------





        mAdapter.notifyDataSetChanged()
        mAdapter.selectedPosition = 0

        dialogue = DialogueYoutube(requireActivity())

        return currentView
    }

    companion object {

        fun newInstance(ctx: Context): MusiquesPasseesFragment {

            val nf = MusiquesPasseesFragment()
            //TODO corriger ça


            val songList: MutableList<Song> = mutableListOf()
            for(hs in nf.songList.list){
                songList.add(hs.song)
            }
            return nf
        }
    }
}