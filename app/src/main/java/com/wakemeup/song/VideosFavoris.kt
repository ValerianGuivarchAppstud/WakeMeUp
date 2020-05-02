package com.wakemeup.song

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.wakemeup.AppWakeUp
import com.wakemeup.MainActivity
import com.wakemeup.R
import com.wakemeup.util.*
import kotlinx.android.synthetic.main.fragment_video.view.*


class VideosFavoris : Fragment() {

    private lateinit var dialogue : DialogueYoutube
    private var isPlaying: Boolean = false
    private val favorisList : MutableList<SongHistorique> = mutableListOf()

    private lateinit var mAdapter: SongHistoriqueAdaptater
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var currentView: View

    private var currentIndex: Int = 0
    private var currentSongLength: Int = 0
    private var firstLaunch = true
    private var currentSong: SongHistorique? = null

    private var youTubePlayer: YouTubePlayer? = null
    private lateinit var textePasDeFavori : TextView


    private fun changeSelectedSong(index: Int) {
        mAdapter.notifyItemChanged(mAdapter.selectedPosition)
        currentIndex = index
        mAdapter.selectedPosition = currentIndex
        mAdapter.notifyItemChanged(currentIndex)
    }

    //lance la video reliée au parametre "song"
    private fun prepareSong(songH: SongHistorique?) {
        //todo vérifier bug ici, si on sélectionne la musique trop vite

        if (songH != null) {
            val song = songH.song
            currentSongLength = song.duration
            currentSong = songH

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
                favorisList.remove(currentSong!!)
                if (favorisList.isNullOrEmpty()){
                    currentSong = null
                    textePasDeFavori.visibility=View.VISIBLE
                }

                //Mise à jour-----------------

                mAdapter.notifyDataSetChanged()
                mAdapter.selectedPosition = 0
                //------------------------------

                resetFavoris(this.requireContext())
                persisteFavoris(this.requireContext(),favorisList)

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
                    dialogue.createDialoguePartage(currentSong!!.song) //lance la dialogue pour preciser le temps
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
        favorisList.clear()

        gestionBoutonSupprimer()
        gestionBoutonParatage()

        textePasDeFavori = currentView.findViewById(R.id.texte_pas_de_favori)

        //Charge les favoris-----------------------------------------------------
        val favoris : MutableList<SongHistorique>? = loadFavoris(this.requireContext())
        if (favoris != null) {
            favorisList.addAll(favoris)
            if (favoris.isEmpty()) {
                textePasDeFavori.visibility = View.VISIBLE
            }
            else{
                textePasDeFavori.visibility=View.INVISIBLE
            }

            //Lancer la 1ere video youtube de la liste
            if (favorisList.isNotEmpty()) {
                currentSong = favorisList[0]
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

        //Initialisation du spinner----------------------------------------------------------------------------
        val spinner = currentView.findViewById<Spinner>(R.id.spinner_trie)
        val trie = arrayOf("Date d'ajout","Alphabétique")

        spinner.adapter = ArrayAdapter<String>(this.requireContext(),android.R.layout.simple_list_item_1,trie)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (trie[position]){
                    "Alphabétique"  -> {
                        trieAlphabetique(favorisList, activity!!)
                        mAdapter.notifyDataSetChanged()
                        mAdapter.selectedPosition = 0
                    }
                    "Date d'ajout" -> {
                        trieDateAjout(favorisList,activity!!)
                        mAdapter.notifyDataSetChanged()
                        mAdapter.selectedPosition = 0
                    }
                }
            }
        }
        //------------------------------------------------------------------------------------------------------

        return currentView
    }

    companion object {

        fun newInstance(ctx: Context): VideosFavoris {

            val nf = VideosFavoris()
            nf.mAdapter = SongHistoriqueAdaptater(
                ctx,
                "FAVORIS",
                nf.favorisList,
                object : SongHistoriqueAdaptater.RecyclerItemClickListener {
                    override fun onClickListener(songH: SongHistorique, position: Int) {
                        nf.firstLaunch = false
                        nf.changeSelectedSong(position)
                        nf.prepareSong(songH)
                    }
                })
            return nf
        }
    }


}


