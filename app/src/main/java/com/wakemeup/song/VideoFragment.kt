package com.wakemeup.song

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.wakemeup.util.loadFavoris
import com.wakemeup.util.persisteFavoris
import com.wakemeup.util.resetFavoris
import kotlinx.android.synthetic.main.fragment_video.*
import kotlinx.android.synthetic.main.fragment_video.view.*


class VideoFragment : Fragment() {

    private lateinit var dialogue : DialogueYoutube
    private var isPlaying: Boolean = false
    private val songList = mutableListOf<Song>()
    private var favorisListe : MutableList<Song> = mutableListOf<Song>()
    private lateinit var mAdapter: SongAdapter
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var currentView: View
    private lateinit var partageView: View

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


    private fun startActivityListFriendToSendMusicActivity(){
        //ouvre l'activity de la liste d'amis
        activity!!.intent = Intent(activity, ListFriendToSendMusicActivity::class.java)
        activity!!.intent.putExtra("song", currentSong as Parcelable)
        startActivity(activity!!.intent)
    }

    //Recherche une musique depuis la barre de recherche (id : et_search)
    private fun createDialogForSearch() {
        val builder = AlertDialog.Builder(activity!!)
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_search, null)
        builder.setTitle(R.string.rechercher)
            .setView(view)
            .setPositiveButton(R.string.ok) { _, _ ->
                val search: String =
                    view.findViewById<EditText>(R.id.et_search).text.toString().trim()
                if (search.isNotEmpty()) {
                    //si la bar de recherche n'est pas vide
                    getSongList(search)
                } else {
                    //si la bar de recherche est vide
                    Toast.makeText(
                        activity!!.application,
                        "Veuillez remplir le champ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.create().show()
    }


    //Gestion du clic sur le bouton partage
    private fun gestionBoutonParatage(){
        val btPartage = currentView.findViewById<Button>(R.id.list_video_partage)
        btPartage.setOnClickListener {
            if (AppWakeUp.auth.currentUser!!.isAnonymous) {
                dialogue.createAlertDialogNotConnected(context!!, this.activity!! as MainActivity)
            } else {
                if (currentSong != null) {
                    dialogue.createDialoguePartage(currentSong) //lance la dialogue pour preciser le temps
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

    //Gestion du clic sur le bouton favori
    private fun gestionBoutonFavori(){
        val btFavori = currentView.findViewById<Button>(R.id.list_video_favori)
        btFavori.setOnClickListener {
            if (AppWakeUp.auth.currentUser!!.isAnonymous) {
                dialogue.createAlertDialogNotConnected(context!!, this.activity!! as MainActivity)
            } else {
                if (currentSong!=null) {
                    if (loadFavoris(this.requireContext()) != null) {
                        favorisListe = loadFavoris(this.requireContext())!!
                    }
                    else{
                        favorisListe = mutableListOf<Song>()
                    }
                    favorisListe.add(currentSong!!)
                    resetFavoris(this.requireContext())
                    persisteFavoris(this.requireContext(),favorisListe)

                }
                Toast.makeText(
                    activity!!.application,
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreate(savedInstanceState)

        currentView = inflater.inflate(R.layout.fragment_video, container, false)
        partageView = inflater.inflate(R.layout.dialog_partage, container, true)

        //Initialisation de la liste des favoris-------------------------------------------------
        val chargement_des_favoris : MutableList<Song>? = loadFavoris(this.requireContext())
        if (chargement_des_favoris != null) {
            favorisListe.addAll(chargement_des_favoris)
        }
        //---------------------------------------------------------------------------------------


        // iv_play.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.selector_play))
        getSongList("musique")


        //Initialisation du recyclerView---------------------------------------------------
        val recyclerView = currentView.findViewById<RecyclerView>(R.id.recycler_list_video)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter
        //---------------------------------------------------------------------------------


        //Gestion des differents boutons--
        gestionBoutonFavori()
        gestionBoutonParatage()
        gestionBoutonSearch()
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

        dialogue = DialogueYoutube(activity!!)

        return currentView
    }

    companion object {

        fun newInstance(ctx: Context): VideoFragment {

            val nf = VideoFragment()
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