package com.wakemeup.song

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.api.services.youtube.model.SearchResult
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.wakemeup.AppWakeUp
import com.wakemeup.MainActivity
import com.wakemeup.R
import com.wakemeup.util.*
import kotlinx.android.synthetic.main.fragment_video.*
import kotlinx.android.synthetic.main.fragment_video.view.*
import java.util.*


class VideoFragment : Fragment() {

    private lateinit var dialogue : DialogueYoutube
    private var isPlaying: Boolean = false
    private val songList = mutableListOf<Song>()
    private var historiqueListe : MutableList<String> = mutableListOf()
    private  var historiqueVideoListe : SongIndex = SongIndex()
    private lateinit var mAdapter: SongAdapter
    private lateinit var rAdapter: RechercheAdaptateur
    private lateinit var hvAdapter : SongHistoriqueAdaptater
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var currentView: View
    private lateinit var partageView: View
    private lateinit var rechercheView: View
    private lateinit var btPartage : Button
    private lateinit var btFavori : Button
    private lateinit var recyclerView : RecyclerView
    private var nbMusiquesChargees : String = "20"
    private var texteCourant : String = "musique"

    private var currentIndex: Int = 0
    private var currentSongLength: Int = 0
    private var currentSong: Song? = null

    private var youTubePlayer: YouTubePlayer? = null



    private fun changeSelectedSong(index: Int) {
        mAdapter.notifyItemChanged(mAdapter.selectedPosition)
        currentIndex = index
        mAdapter.selectedPosition = currentIndex
        mAdapter.notifyItemChanged(currentIndex)
    }

    private fun getSongList(query: String, nbRecherche : String) {

        if (query.isNotEmpty()) {
            SearchYouTube(this).execute(
                query,
                nbRecherche
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
        val referenceUsername =
            AppWakeUp.database.getReference("Users").child(AppWakeUp.auth.currentUser!!.uid)

        referenceUsername.addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val reference =  AppWakeUp.database.getReference("Favoris")
                    val currentuser = AppWakeUp.auth.currentUser!!

                    //Initi de la date d'enregistrmeent des favoris-------------------
                    val calendar : Calendar = Calendar.getInstance()

                    val jour = calendar.get(Calendar.DAY_OF_MONTH)
                    val mois = calendar.get(Calendar.MONTH) +1
                    val anne = calendar.get(Calendar.YEAR)

                    val heure = calendar.get(Calendar.HOUR_OF_DAY)
                    val minute = calendar.get(Calendar.MINUTE)
                    val seconde = calendar.get(Calendar.SECOND)
                    //-----------------------------------------------------------------

                    val favoriSave = Favoris("$jour/$mois/$anne/$heure/$minute/$seconde", currentSong!!, currentuser.uid )
                    var refpush = reference.push()
                    var key = refpush.key!!
                    refpush.setValue(favoriSave).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.i("VideoFragment","demande ajoutée")
                            Log.i("VideoFragment", key)
                        } else {
                            Log.i("VideoFragment","erreur demande ajoutée")
                        }
                    }
                }
            }
        )
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
                    nbMusiquesChargees = "20"
                    texteCourant = search
                    getSongList(texteCourant, nbMusiquesChargees)
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

                    nbMusiquesChargees = "20"
                    texteCourant = recherche
                    getSongList(recherche,nbMusiquesChargees)
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
                    if (youTubePlayerView.visibility == View.GONE) { // rendre le youtubeplayer et les bouton visible
                        youTubePlayerView.visibility = View.VISIBLE
                        btFavori.visibility = View.VISIBLE
                        btPartage.visibility = View.VISIBLE
                    }
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

        btFavori.setOnClickListener {
            if (AppWakeUp.auth.currentUser!!.isAnonymous) {
                dialogue.createAlertDialogNotConnected(requireContext(), this.requireActivity() as MainActivity)
            } else {
                if (currentSong!=null) {
                    gestionFavoris()

                    //TODO les mettre sur firebase


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
        getSongList(texteCourant,"20")


        //Initialisation du recyclerView (Le principal, pour les vidéos youtube)---------------------------------
        mAdapter = SongAdapter(this.requireContext(),songList,
            object : SongAdapter.RecyclerItemClickListener {
                override fun onClickListener(song: Song, position: Int) {
                    if (youTubePlayerView.visibility == View.GONE) { // rendre le youtubeplayer et les bouton visible
                        youTubePlayerView.visibility = View.VISIBLE
                        btFavori.visibility = View.VISIBLE
                        btPartage.visibility = View.VISIBLE
                    }
                    changeSelectedSong(position)
                    prepareSong(song)
                    saveSongInHistVideo(song)
                }
            })
        recyclerView = currentView.findViewById(R.id.recycler_list_video)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter
        //--------------------------------------------------------------------------------------------------------



        //Si on est a la fin du recycler view---------------------------------------------------
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    nbMusiquesChargees = (nbMusiquesChargees.toInt() + 20).toString()
                    getSongList(texteCourant,nbMusiquesChargees)
                }
            }
        })
        //---------------------------------------------------------------------------------------



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

        //Gestion des differents boutons----------------------------------------
        btFavori = currentView.findViewById<Button>(R.id.list_video_favori)
        btPartage = currentView.findViewById<Button>(R.id.list_video_partage)

        gestionBoutonFavori()
        gestionBoutonParatage()
        gestionBoutonSearch()
        gestionBoutonHistorique()
        //-----------------------------------------------------------------------

        //Enlever le youyube player et le bouton au debut---
        youTubePlayerView.visibility = View.GONE
        btFavori.visibility = View.GONE
        btPartage.visibility = View.GONE
        //--------------------------------------------------


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