package com.wakemeup.song

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.contains
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
import com.wakemeup.util.*
import kotlinx.android.synthetic.main.fragment_video.*
import kotlinx.android.synthetic.main.fragment_video.view.*
import kotlinx.android.synthetic.main.item_list_history.*


class VideoFragment : Fragment() {

    private lateinit var dialogue : DialogueYoutube
    private var isPlaying: Boolean = false
    private val songList = mutableListOf<Song>()
    private var favorisListe : SongIndex = SongIndex()
    private var historiqueListe : MutableList<String> = mutableListOf()
    private  var historiqueVideoListe : SongIndex = SongIndex()
    private lateinit var mAdapter: SongAdapter
    private lateinit var rAdapter: RechercheAdaptateur
    private lateinit var hvAdapter : SongHistoriqueAdaptater
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var currentView: View
    private lateinit var partageView: View
    private lateinit var rechercheView: View

    private var currentIndex: Int = 0
    private var currentSongLength: Int = 0
    var firstLaunch = true
    private var currentSong: Song? = null

    private var youTubePlayer: YouTubePlayer? = null



    private fun changeSelectedSong(index: Int) {
        mAdapter.notifyItemChanged(mAdapter.selectedPosition)
        currentIndex = index
        mAdapter.selectedPosition = currentIndex
        mAdapter.notifyItemChanged(currentIndex)
    }

    private fun getSongList(query: String) {

        if (query.isNotEmpty()) {
            SearchYouTube(this).execute(
                query,
                "20"
            )//TODO ajout de settings pour paramêtrer le nb max de chanson à afficher
        }

        if (pb_main_loader != null) {
            pb_main_loader.visibility = View.VISIBLE
        }
    }

    fun updateAff(responses: MutableList<SearchResult>) {

        val songs = mutableListOf<Song>()
        for ((i, track) in responses.withIndex()) {
            songs.add(songs.size, Song(track, i))
        }
        currentIndex = 0
        currentView.pb_main_loader.visibility = View.GONE
        songList.clear()
        songList.addAll(songs)
        mAdapter.notifyDataSetChanged()
        mAdapter.selectedPosition = 0

        //Lancer la 1ere video youtube de la liste
        currentSong = songList[0]
        changeSelectedSong(0)
        prepareSong(currentSong)
        //----------------------------------------
    }


    //Sauvgarde une vidéo dans l'hisotrique des vidéos quand on clique dessus
    fun saveSongInHistVideo(song : Song){
        var load = loadHistoriqueVideo(this.requireContext())
        if (load != null){
            if (load.index == 0){
                load.list = mutableListOf()
            }
            load.list.add(SongHistorique(load.index,song))

            historiqueVideoListe.list.clear()
            historiqueVideoListe.list.addAll(load.list)
            historiqueVideoListe.index = load.index +1
        }
        else{
            historiqueVideoListe.list.add(SongHistorique(0,song))
            historiqueVideoListe.index = 1
        }

        trieDateAjout(historiqueVideoListe.list, this.requireContext())
        persisteHistoriqueVideo(this.requireContext(), historiqueVideoListe)
    }


    //lance la video reliée au parametre "song"
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
                this.isPlaying = true
            }
        }
    }

    private fun gestionHistorique(search : String){
        //Gestion de la persistance de l'historique (sauvgarde l'historique dans un fichier)
        if (loadHistorique(this.requireContext()) != null) {
            historiqueListe = loadHistorique(this.requireContext())!!
        }
        else{
            historiqueListe = mutableListOf<String>()
        }
        historiqueListe.add(search)
        resetHistorique(this.requireContext())
        persisteHistorique(this.requireContext(),historiqueListe)

    }

    private fun gestionFavoris(){
        //Gestion de la persistance des favoris (sauvgarde les favoris dans un fichier)
        var index : Int
        val listeTemp = loadFavoris(this.requireContext())
        if (listeTemp!=null) {
            if (listeTemp.index !=0) {
                favorisListe = listeTemp
            }
        }

        favorisListe.list.add(SongHistorique(favorisListe.index,currentSong!!))
        favorisListe.index++
        resetFavoris(this.requireContext())
        persisteFavoris(this.requireContext(),favorisListe)
    }



    //Recherche une musique depuis la barre de recherche (id : et_search)
    private fun createDialogForSearch() {
        val builder = AlertDialog.Builder(requireActivity())
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_search, null)
        val recherches = loadHistorique(this.requireContext())
        val recyclerViewRecherche = view.findViewById<RecyclerView>(R.id.recycler_list_recherche)
        var parametre : MutableList<String>?


        //Créer le dialogue de recherche de vidéo-------------------------------------------------
        val dialogueSearch =
            builder
            .setView(view)
            .setPositiveButton(R.string.ok) { _, _ ->
                val search: String = view.findViewById<EditText>(R.id.et_search).text.toString().trim()
                if (search.isNotEmpty()) {
                    getSongList(search)
                    //Gestion de la persistance de l'historique------------------
                    gestionHistorique(search)
                    //-----------------------------------------------------------
                }
                else {
                    //si la bar de recherche est vide
                    Toast.makeText(
                        requireActivity().application,
                        "Veuillez remplir le champ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton("Annuler"){ _, _ ->
            }
            .create()
        //-----------------------------------------------------------------------------------------

        //Initialisation du recyclerView de l'historique---------------------------
        recyclerViewRecherche.layoutManager = LinearLayoutManager(requireActivity())
        if (recherches != null) {
            parametre = recherches
        }
        else{
            parametre = mutableListOf<String>()
        }
        //--------------------------------------------------------------------------

        //Initialisiton de l'adaptateur de recycler view-----------------------------
        rAdapter = RechercheAdaptateur(parametre,
            object : RechercheAdaptateur.RecyclerItemClickListener{
                override fun onClickListener(recherche : String, position: Int){
                    getSongList(recherche)
                    dialogueSearch.hide()
                }
            }
        )
        recyclerViewRecherche.adapter = rAdapter
        //---------------------------------------------------------------------------

        dialogueSearch.show()

    }



    //Gestion du dialogue de l'historique
    private fun createDialogForHistory(){
        val builder = AlertDialog.Builder(requireActivity())
        val view = requireActivity().layoutInflater.inflate(R.layout.dialogue_history, null)
        val recyclerViewRecherche = view.findViewById<RecyclerView>(R.id.recycler_list_historique_video)


        //Rest l'hsitorique des videos----------------------------------
        historiqueVideoListe.list.clear()
        val histVideoTemp = loadHistoriqueVideo(this.requireContext())
        if (histVideoTemp == null)
            historiqueVideoListe.list = mutableListOf()
        else
            historiqueVideoListe = histVideoTemp
        //---------------------------------------------------------------

        //Initialisation du dialogue pour l'historique----
        val dialogueHistory =
            builder
                .setView(view)
                .setNegativeButton("Annuler") { _, _ ->
                    trieDateAjout(historiqueVideoListe.list,requireActivity())
                }
                .create()
        //------------------------------------------------

        //Initialisation du recyclerView de l'historique---------------------------
        recyclerViewRecherche.layoutManager = LinearLayoutManager(view.context!!)
        //--------------------------------------------------------------------------

        //Initialisiton de l'adaptateur de recycler view-----------------------------
        hvAdapter = SongHistoriqueAdaptater(
            this.requireContext(),
            "HISTORIQUE",
            historiqueVideoListe.list,
            object : SongHistoriqueAdaptater.RecyclerItemClickListener{
                override fun onClickListener(song : SongHistorique, position: Int){
                    prepareSong(song.song)
                    dialogueHistory.hide()
                }
            }
        )
        recyclerViewRecherche.adapter = hvAdapter
        //---------------------------------------------------------------------------

        //Initialisation du spinner----------------------------------------------------------------------------
        val spinner = view.findViewById<Spinner>(R.id.spinner_trie)
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
                        trieAlphabetique(historiqueVideoListe.list, activity!!)
                        hvAdapter.notifyDataSetChanged()
                        hvAdapter.selectedPosition = 0
                    }
                    "Date d'ajout" -> {
                        trieDateAjout(historiqueVideoListe.list,activity!!)
                        hvAdapter.notifyDataSetChanged()
                        hvAdapter.selectedPosition = 0
                    }
                }
            }
        }
        //------------------------------------------------------------------------------------------------------
        dialogueHistory.show()
    }


    //Gestion du clic sur le bouton partage
    private fun gestionBoutonParatage(){
        val btPartage = currentView.findViewById<Button>(R.id.list_video_partage)
        btPartage.setOnClickListener {
            if (AppWakeUp.auth.currentUser!!.isAnonymous) {
                dialogue.createAlertDialogNotConnected(requireContext(), this.requireActivity() as MainActivity)
            } else {
                if (currentSong != null) {
                    dialogue.createDialoguePartage(currentSong) //lance la dialogue pour preciser le temps
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

    //Gestion du clic sur le bouton favori
    private fun gestionBoutonFavori(){
        val btFavori = currentView.findViewById<Button>(R.id.list_video_favori)
        btFavori.setOnClickListener {
            if (AppWakeUp.auth.currentUser!!.isAnonymous) {
                dialogue.createAlertDialogNotConnected(requireContext(), this.requireActivity() as MainActivity)
            } else {
                if (currentSong!=null) {
                    gestionFavoris()
                }
                Toast.makeText(
                    requireActivity().application,
                    "La video a été ajouté",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    //Gestion du clic sur le bouton rechercher
    private fun gestionBoutonSearch(){
        val bt_search = currentView.findViewById<FloatingActionButton>(R.id.fab_search)
        bt_search.setOnClickListener {
            createDialogForSearch()
        }
    }

    //Gestion du clic sur le bouton Historique
    private fun gestionBoutonHistorique(){
        val bt_Historique = currentView.findViewById<FloatingActionButton>(R.id.fab_history)
        bt_Historique.setOnClickListener {
            createDialogForHistory()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreate(savedInstanceState)
        //Initialisation des vues-------------------------------------------------------------
        currentView = inflater.inflate(R.layout.fragment_video, container, false)
        partageView = inflater.inflate(R.layout.dialog_partage, container, false)
        rechercheView = inflater.inflate(R.layout.dialogue_history, container, false)
        //------------------------------------------------------------------------------------


        //Initialisation de la liste des favoris-------------------------------------------------
        val chargement_des_favoris = loadFavoris(this.requireContext())
        if (chargement_des_favoris != null) {
            favorisListe = chargement_des_favoris
        }
        //---------------------------------------------------------------------------------------

        //Initialisation de la liste contenant l'historique-------------------------------------
        val chargement_historique : MutableList<String>? = loadHistorique(this.requireContext())
        if (chargement_historique != null) {
            historiqueListe.addAll(chargement_historique)
        }
        //---------------------------------------------------------------------------------------

        //Initialisation de la liste contenant l'historique-------------------------------------
        val chargement_historiqueVideo = loadHistoriqueVideo(this.requireContext())
        if (chargement_historiqueVideo != null) {
            historiqueVideoListe = chargement_historiqueVideo
        }
        //---------------------------------------------------------------------------------------


        // iv_play.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.selector_play))
        getSongList("musique")


        //Initialisation du recyclerView (Le principal, pour les vidéos youtube)------------
        mAdapter = SongAdapter(this.requireContext(),songList,
            object : SongAdapter.RecyclerItemClickListener {
                override fun onClickListener(song: Song, position: Int) {
                    //nfirstLaunch = false
                    changeSelectedSong(position)
                    prepareSong(song)
                    saveSongInHistVideo(song)
                }
            })
        val recyclerView = currentView.findViewById<RecyclerView>(R.id.recycler_list_video)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter

        //---------------------------------------------------------------------------------


        //Gestion des differents boutons--
        gestionBoutonFavori()
        gestionBoutonParatage()
        gestionBoutonSearch()
        gestionBoutonHistorique()
        //--------------------------------


        //Initialisation du YoutubePlayer----------------------------------------------------------
        youTubePlayerView = currentView.findViewById(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView)
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(ytPlayer: YouTubePlayer) {
                youTubePlayer = ytPlayer
                if (currentSong != null) {
                    prepareSong(currentSong)
                }
            }
        })

        //-----------------------------------------------------------------------------------------


        //Initialisation de la variable dialogue de type DialogueYoutube------------------------
        // (comporte presque tout les dialogues géneré pour la partie Youtube de l'application)
        dialogue = DialogueYoutube(requireActivity())
        //--------------------------------------------------------------------------------------

        return currentView
    }

    companion object {

        fun newInstance(ctx: Context): VideoFragment {
            val nf = VideoFragment()
            return nf
        }
    }
}