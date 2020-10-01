package com.vguivarc.wakemeup.song.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.repo.ViewModelFactory
import com.vguivarc.wakemeup.song.Song
import com.vguivarc.wakemeup.song.SongAdapter
import com.vguivarc.wakemeup.song.favori.FavorisViewModel
import com.vguivarc.wakemeup.util.SortedListType
import com.vguivarc.wakemeup.util.Utility
import kotlinx.android.synthetic.main.fragment_video.view.*
import timber.log.Timber


class RechercheVideoFragment : Fragment(), HistoriqueAdapter.RechercheVideoAdapterListener, SongAdapter.SongItemClickListener  {

    private lateinit var hvAdapter : HistoriqueAdapter
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var ensembleVideo: ConstraintLayout
    private lateinit var btPartage : Button
    private lateinit var btFavori : Button
    private lateinit var currentView: View
    private lateinit var partageView: View
    private lateinit var rechercheView: View
    private lateinit var recyclerView : RecyclerView

    private var currentIndex: Int = 0
    private var currentSong: Song? = null

    private var youTubePlayer: YouTubePlayer? = null
    private lateinit var textPasDeRecherche : TextView


    //validé
    private val searchVideosList = mutableListOf<Song>()
    private val historiqueSearch = mutableListOf<String>()
    private lateinit var searchVideoAdapter: SongAdapter
    private lateinit var viewModelSearchVideo : RechercheVideoViewModel
    private lateinit var viewModelFavori : FavorisViewModel
    private lateinit var pbMainLoader : ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreate(savedInstanceState)
        requireActivity().title = "Musiques"
        val factory = ViewModelFactory(AppWakeUp.repository)
        viewModelSearchVideo = ViewModelProvider(this, factory).get(RechercheVideoViewModel::class.java)
        viewModelFavori = ViewModelProvider(this, factory).get(FavorisViewModel::class.java)


        //Initialisation des vues-------------------------------------------------------------
        currentView = inflater.inflate(R.layout.fragment_video, container, false)
        partageView = inflater.inflate(R.layout.dialog_partage, container, false)
        rechercheView = inflater.inflate(R.layout.dialogue_history, container, false)
        //------------------------------------------------------------------------------------
        textPasDeRecherche = currentView.findViewById(R.id.textPasDeRecherche)
        rechercheView.visibility = View.GONE
        pbMainLoader = currentView.findViewById(R.id.pb_main_loader)
        searchVideoAdapter = SongAdapter(requireContext(), searchVideosList, this)

        viewModelSearchVideo.getRechercheVideosLiveData().observe(requireActivity(), {
            if(it.error==null) {
                if(it.searchList.size==0){
                    searchVideosList.clear()
                    textPasDeRecherche.visibility = View.VISIBLE
                    rechercheView.visibility = View.GONE
                } else {
                    searchVideosList.clear()
                    searchVideosList.addAll(it.searchList)
                    searchVideoAdapter.notifyDataSetChanged()
                    textPasDeRecherche.visibility = View.GONE
                    rechercheView.visibility = View.VISIBLE
                }
            }
            pbMainLoader.visibility=View.GONE
        })


        hvAdapter = HistoriqueAdapter(historiqueSearch, this)

        recyclerView = currentView.findViewById(R.id.recycler_list_video)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = searchVideoAdapter
        //--------------------------------------------------------------------------------------------------------



        //Si on est a la fin du recycler view---------------------------------------------------
      /*  recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    viewModelSearchVideo.addSearchVideo()
                }
            }
        })*/
        //---------------------------------------------------------------------------------------



        //Initialisation du YoutubePlayer----------------------------------------------------------
        youTubePlayerView = currentView.findViewById(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView)
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                this@RechercheVideoFragment.youTubePlayer = youTubePlayer
                viewModelSearchVideo.getCurrentSong().observe (requireActivity(), { song ->
                    if (song != null) {
                        currentSong = song

                        if (this@RechercheVideoFragment.youTubePlayer != null) {
                            this@RechercheVideoFragment.youTubePlayer!!.loadVideo(song.id,0f)
                            this@RechercheVideoFragment.youTubePlayer!!.setVolume(100)
                            youTubePlayerView.getPlayerUiController()
                                .setVideoTitle(song.title)
                        }
                    }
                })
            }
        })

        //-----------------------------------------------------------------------------------------

        //Gestion des differents boutons----------------------------------------
        ensembleVideo = currentView.findViewById(R.id.EnsembleVideo)
        btFavori = currentView.findViewById(R.id.list_video_favori)
        btPartage = currentView.findViewById(R.id.list_video_partage)

        gestionBoutonFavori()
        gestionBoutonPartage()
        gestionBoutonSearch()
        gestionBoutonHistorique()
        //-----------------------------------------------------------------------

        //Enlever le youyube player et le bouton au debut---
        ensembleVideo.visibility = View.GONE
        //--------------------------------------------------

        //--------------------------------------------------------------------------------------

        return currentView
    }

/*

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
*/




    //Recherche une musique depuis la barre de recherche (id : et_search)
    private fun createDialogForSearch() {
        val builder = AlertDialog.Builder(requireActivity())
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_search, null)

        //Créer le dialogue de recherche de vidéo-------------------------------------------------
        val dialogueSearch =
            builder
            .setView(view)
            .setPositiveButton(R.string.ok) { _, _ ->
                val search: String = view.findViewById<EditText>(R.id.et_search).text.toString().trim()
                if (search.isNotEmpty()) {
                    currentView.pb_main_loader.visibility = View.VISIBLE
                    textPasDeRecherche.visibility=View.GONE
                    viewModelSearchVideo.searchVideos(search)
                }
                else {
                    Utility.createSimpleToast("Veuillez remplir le champ")
                }
            }
            .setNegativeButton("Annuler"){ _, _ ->
            }
            .create()
        dialogueSearch.show()
    }



    lateinit var dialogueHistory : AlertDialog
    //Gestion du dialogue de l'historique
    private fun createDialogForHistory(){
        val builder = AlertDialog.Builder(requireActivity())
        val view = requireActivity().layoutInflater.inflate(R.layout.dialogue_history, null)
        val recyclerViewRecherche = view.findViewById<RecyclerView>(R.id.recycler_list_historique_video)



        //Initialisation du dialogue pour l'historique----
        dialogueHistory =
            builder
                .setView(view)
                .setNegativeButton("Annuler") { _, _ ->}
                .setOnItemSelectedListener(object : OnItemSelectedListener {
                    @SuppressLint("LogNotTimber")
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View,
                        position: Int, id: Long
                    ) {
                        if (ensembleVideo.visibility == View.GONE) { // rendre le youtubeplayer et les bouton visible
                            ensembleVideo.visibility = View.VISIBLE
                        }
                        viewModelSearchVideo.searchVideos(historiqueSearch[position])
                        dialogueHistory.hide()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                })
                .create()
        //------------------------------------------------



        viewModelSearchVideo.getSortedHistoriqueSearchLiveData().observe(requireActivity(),
            { historiqueVideoListe ->
                historiqueSearch.clear()
                historiqueSearch.addAll(historiqueVideoListe)
                hvAdapter.notifyDataSetChanged()
            })

        //Initialisation du recyclerView de l'historique---------------------------
        recyclerViewRecherche.layoutManager = LinearLayoutManager(view.context!!)


        recyclerViewRecherche.adapter = hvAdapter
        //---------------------------------------------------------------------------

        //Initialisation du spinner----------------------------------------------------------------------------
        val spinner = view.findViewById<Spinner>(R.id.spinner_trie)
        val trie = arrayOf("Date d'ajout","Alphabétique")

        spinner.adapter = ArrayAdapter(this.requireContext(),android.R.layout.simple_list_item_1,trie)
        spinner.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (trie[position]){
                    "Alphabétique"  -> {
                        viewModelSearchVideo.changeSortedType(SortedListType.Alphabetique)
                    }
                    "Date d'ajout" -> {
                        viewModelSearchVideo.changeSortedType(SortedListType.Date)
                    }
                }
            }
        }
        dialogueHistory.show()
        }


    //Gestion du clic sur le bouton partage
    private fun gestionBoutonPartage(){
       btPartage.setOnClickListener {
            if (AppWakeUp.repository.getCurrentUser()==null) {
            //TODO remplacer par alertdialog (en commentaire) pour envoyer vers le fragment de connection
                Utility.createSimpleToast(AppWakeUp.appContext.resources.getString(R.string.vous_netes_pas_connecte))
            } else {
                if (currentSong != null) {
                    val action = RechercheVideoFragmentDirections.actionRechercheVideoFragmentToContactsListeShareFragment(currentSong!!)
                    findNavController().navigate(action)
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
    private fun gestionBoutonFavori() {
        btFavori.setOnClickListener {
            if (AppWakeUp.repository.getCurrentUser()==null) {
                Utility.createSimpleToast(AppWakeUp.appContext.resources.getString(R.string.vous_netes_pas_connecte))
            } else {
                if (currentSong != null) {
                    viewModelFavori.addFavori(currentSong!!)
                } else {
                    Utility.createSimpleToast(AppWakeUp.appContext.resources.getString(R.string.aucune_video_selectionnee))
                }
            }
        }
        viewModelFavori.getAddFavoriStateLiveData().observe(requireActivity(), {
            if(it.error!=null){
                if(it.error.equals("AlreadyExistingFavori")){
                    Utility.createSimpleToast("Favori déjà enregistré")
                } else
                {
                    Utility.createSimpleToast("Erreur dans l'ajout du favori")
                }
                Timber.e("RechercheVideoFragment: ${it.error.toString()}")
            } else {
                Utility.createSimpleToast("Nouveau favori ajouté à votre liste")
            }
        })
    }

    //Gestion du clic sur le bouton rechercher
    private fun gestionBoutonSearch(){
        val btSearch = currentView.findViewById<FloatingActionButton>(R.id.fab_search)
        btSearch.setOnClickListener {
            createDialogForSearch()
        }
    }

    //Gestion du clic sur le bouton Historique
    private fun gestionBoutonHistorique(){
        val btHistorique = currentView.findViewById<FloatingActionButton>(R.id.fab_history)
        btHistorique.setOnClickListener {
            createDialogForHistory()
        }
    }

    @SuppressLint("LogNotTimber")
    override fun onHistoriqueClicked(recherche: String, itemView: View) {
        if (ensembleVideo.visibility == View.GONE) { // rendre le youtubeplayer et les bouton visible
            ensembleVideo.visibility = View.VISIBLE
        }
        viewModelSearchVideo.searchVideos(recherche)
        dialogueHistory.hide()
    }
//TODO historique marche pas, et inversion alphabétique et date ajout
    //TODO quand on descend ça marche pas
    //TODO remettre le dernier truc cherché dans la barre de recherche
    @SuppressLint("LogNotTimber")
    override fun onSongClickListener(position: Int) {
        if (ensembleVideo.visibility == View.GONE) { // rendre le youtubeplayer et les bouton visible
            ensembleVideo.visibility = View.VISIBLE
        }
        searchVideoAdapter.notifyItemChanged(searchVideoAdapter.selectedPosition)
        currentIndex = position
        searchVideoAdapter.selectedPosition = currentIndex
        searchVideoAdapter.notifyItemChanged(currentIndex)
        viewModelSearchVideo.setCurrentSong(position)
    }
}