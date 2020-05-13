package com.wakemeup.song

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import kotlinx.android.synthetic.main.fragment_update_email.view.*
import kotlinx.android.synthetic.main.fragment_video.view.*
import java.sql.Timestamp


class VideosFavoris : Fragment() {

    private lateinit var dialogue : DialogueYoutube
    private var isPlaying: Boolean = false
    private var favorisList : MutableList<Favoris> = mutableListOf()
    private lateinit var fAdapter: FavorisAdaptater
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var currentView: View

    private var currentIndex: Int = 0
    private var currentSongLength: Int = 0
    private var currentSong: Song? = null

    private var youTubePlayer: YouTubePlayer? = null
    private lateinit var btSupprimer : Button
    private lateinit var btPartage : Button
    private lateinit var textePasDeFavori : TextView
    private val TAG = "VideosFavoris"
    private var ajout = true


    private fun changeSelectedSong(index: Int) {
        fAdapter.notifyItemChanged(fAdapter.selectedPosition)
        currentIndex = index
        fAdapter.selectedPosition = currentIndex
        fAdapter.notifyItemChanged(currentIndex)
    }

    //lance la video reliée au parametre "song"
    private fun prepareSong(song: Song) {
        //todo vérifier bug ici, si on sélectionne la musique trop vite
        if (song != null) {
            currentSongLength = song.duration
            currentSong = song

            if (youTubePlayer != null) {
                youTubePlayer!!.loadVideo(song.id, 0F)
                youTubePlayer!!.setVolume(100)
                youTubePlayerView.getPlayerUiController()
                    .setVideoTitle(song.title)//.loadVideo(videoId, 0F)

                this.isPlaying = true
            }
        }
    }

    //TODO créer classe abstraite ou créer classe BouttonPartage

    //Gestion du clic sur le bouton suprimer
    private fun gestionBoutonSupprimer(){

        btSupprimer.setOnClickListener {
        var listSong = mutableListOf<Any>()
            if (currentSong != null) {
                ajout = false
                //Suprimer de firebase
                AppWakeUp.database.getReference("Favoris")
                    .addValueEventListener(
                        object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {}
                            override fun onDataChange(p0: DataSnapshot) {
                                for (snapshot in p0.children) {
                                    if (snapshot.key != null) {
                                        val ref = AppWakeUp.database.getReference("Favoris").child(snapshot.key as String)
                                            ref.addValueEventListener(
                                                object : ValueEventListener {
                                                    override fun onCancelled(p1: DatabaseError) {}

                                                    override fun onDataChange(p1: DataSnapshot) {
                                                        for (snapshot2 in p1.children) {
                                                            if (snapshot2.key == "idSong"){
                                                                var mapSong =
                                                                    snapshot2.value as HashMap<*, *>
                                                                for (elem in mapSong.values) {
                                                                    listSong.add(elem)
                                                                }
                                                                val idTrouvee = listSong[4] as String

                                                                if (idTrouvee == currentSong!!.id){ // si l'id de la musique trouvé = a celle du currentSong
                                                                    var favASupprimer : Favoris? = null
                                                                    for(e in favorisList){
                                                                        if(e.idSong.id == idTrouvee){
                                                                            favASupprimer = e
                                                                        }
                                                                    }
                                                                    if (favASupprimer!=null)
                                                                        favorisList.remove(favASupprimer)

                                                                    fAdapter.selectedPosition = 0
                                                                    currentIndex = 0
                                                                    fAdapter.notifyDataSetChanged()
                                                                    if(favorisList.isNullOrEmpty()){
                                                                        textePasDeFavori.visibility = View.VISIBLE
                                                                    }
                                                                    ref.removeValue()
                                                                }
                                                                listSong.clear()
                                                            }
                                                        }
                                                    }
                                                }
                                            )
                                    }
                                }
                            }
                        }
                    )

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

    //Gestion du clic sur le bouton partage
    private fun gestionBoutonParatage(){

        btPartage.setOnClickListener {
            if (AppWakeUp.auth.currentUser!!.isAnonymous) {
                dialogue.createAlertDialogNotConnected(requireContext(), this.requireActivity() as MainActivity)
            } else {
                if (currentSong != null) {
                    dialogue.createDialoguePartage(currentSong!!) //lance la dialogue pour preciser le temps
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


    fun chargerFavoris(){

        //TODO modifier pour seulement charger les favoris de l'utilisateur (avec un equalTo)
        favorisList.clear()
        var fin = false
        ajout = true
        var appartientA =""
        var index = ""
        var song : Song? = null
        var listSong = mutableListOf<Any>()

        textePasDeFavori.visibility = View.VISIBLE

        AppWakeUp.database.getReference("Favoris")
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onDataChange(p0: DataSnapshot) {
                        //boucle sur les id des sonneries
                        for (snapshot in p0.children) {
                            if(fin == true){
                                break
                            }
                            if (snapshot.key != null) {
                                    val refFavoris = AppWakeUp.database.getReference("Favoris")
                                        .child(snapshot.key as String).orderByChild(AppWakeUp.auth.currentUser?.uid!!)
                                    refFavoris.addValueEventListener(
                                        object : ValueEventListener {
                                            override fun onCancelled(p1: DatabaseError) {}

                                            override fun onDataChange(p1: DataSnapshot) {
                                                if(ajout == true){
                                                    loop@ for (snapshot2 in p1.children) {
                                                        //boucle sur les des favoris
                                                        if (snapshot2.key != null) {
                                                            when (snapshot2.key) {
                                                                "appartientA" -> {
                                                                    appartientA = snapshot2.value as String
                                                                    if (appartientA !=AppWakeUp.auth.currentUser?.uid!!){
                                                                        break@loop
                                                                        fin=true
                                                                    }
                                                                }
                                                                "index" -> index =
                                                                    snapshot2.value as String
                                                                "idSong" -> {
                                                                    var mapSong =
                                                                        snapshot2.value as HashMap<*, *>
                                                                    for (elem in mapSong.values) {
                                                                        listSong.add(elem)
                                                                    }
                                                                    song = Song(
                                                                        listSong[4] as String,
                                                                        listSong[5] as String,
                                                                        listSong[1] as String,
                                                                        listSong[2] as String,
                                                                        (listSong[0] as Long).toInt(),
                                                                        (listSong[3] as Long).toInt()
                                                                    )
                                                                    listSong.clear()
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if (song!=null && appartientA == AppWakeUp.auth.currentUser!!.uid) { // TODO moddifier le if (1ere TODO)
                                                        val fav = Favoris(index, song!!, appartientA)
                                                        favorisList.add(fav)
                                                        if (activity != null)
                                                            trieDateAjoutFavoris(favorisList,activity!!)

                                                        textePasDeFavori.visibility = View.INVISIBLE
                                                        fAdapter.notifyDataSetChanged()
                                                        fAdapter.selectedPosition = 0
                                                    }
                                                }
                                            }
                                        }
                                    )

                            }
                        }
                        val pb_main_loader = currentView.findViewById<ProgressBar>(R.id.pb_main_loader)
                        pb_main_loader.visibility = View.GONE
                    }
                }
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        currentView = inflater.inflate(R.layout.fragment_favori, container, false)

        //Initialisation du recyclerView (Le principal, pour les vidéos youtube)----------------------------
        fAdapter = FavorisAdaptater(
            this.requireContext(),
            favorisList,
            object : FavorisAdaptater.RecyclerItemClickListener {
                override fun onClickListener(songf: Favoris, position: Int) {
                    if (youTubePlayerView.visibility == View.GONE) { // rendre le youtubeplayer et les bouton visible
                        youTubePlayerView.visibility = View.VISIBLE
                        btSupprimer.visibility = View.VISIBLE
                        btPartage.visibility = View.VISIBLE
                    }
                    changeSelectedSong(position)
                    prepareSong(songf.idSong)
                }
            })
        val recyclerView = currentView.findViewById<RecyclerView>(R.id.recycler_list_video_favoris)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = fAdapter
        //---------------------------------------------------------------------------------------------------

        currentSong = null

        //Init du youtube player--------------------------------------------------------------------
        youTubePlayerView = currentView.findViewById(R.id.youtube_player_view_favoris)
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(ytPlayer: YouTubePlayer) {
                youTubePlayer = ytPlayer
                if (currentSong != null) {
                    prepareSong(currentSong!!)
                }
            }
        })
        //------------------------------------------------------------------------------------------


        currentIndex = 0
        favorisList.clear()
        textePasDeFavori = currentView.findViewById(R.id.texte_pas_de_favori)

        //Charge les favoris--------------------------------------------------------------
        chargerFavoris()
        //---------------------------------------------------------------------------------


        //Gestion des boutons-----------------------------------------------------------
        btSupprimer = currentView.findViewById<Button>(R.id.bouton_supprimer_favori)
        btPartage = currentView.findViewById<Button>(R.id.button_partage_favori)
        gestionBoutonSupprimer()
        gestionBoutonParatage()
        //------------------------------------------------------------------------------

        //Enlever le youyube player et le bouton au debut---
        youTubePlayerView.visibility = View.GONE
        btSupprimer.visibility = View.GONE
        btPartage.visibility = View.GONE
        //--------------------------------------------------


        //Initialisation du spinner----------------------------------------------------------------------------
        val spinner = currentView.findViewById<Spinner>(R.id.spinner_trie)
        val trie = arrayOf("Date d'ajout","Alphabétique")

        spinner.adapter = ArrayAdapter(this.requireContext(),android.R.layout.simple_list_item_1,trie)
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
                    //TODO adapter la liste des favoris à Song index

                    "Alphabétique"  -> {
                        trieAlphabetiqueFavoris(favorisList, activity!!)
                        fAdapter.notifyDataSetChanged()
                        fAdapter.selectedPosition = 0
                    }
                    "Date d'ajout" -> {
                        trieDateAjoutFavoris(favorisList,activity!!)
                        fAdapter.notifyDataSetChanged()
                        fAdapter.selectedPosition = 0
                    }
                }
            }
        }
        //------------------------------------------------------------------------------------------------------

        fAdapter.notifyDataSetChanged()
        fAdapter.selectedPosition = 0

        dialogue = DialogueYoutube(requireActivity())

        return currentView
    }

    companion object {
        fun newInstance(ctx: Context): VideosFavoris {
            val nf = VideosFavoris()
            return nf
        }
    }


}


