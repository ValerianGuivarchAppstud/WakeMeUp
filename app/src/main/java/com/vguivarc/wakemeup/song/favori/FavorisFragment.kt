package com.vguivarc.wakemeup.song.favori

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.repo.ViewModelFactory
import com.vguivarc.wakemeup.song.Song


class FavorisFragment : Fragment(),  FavorisAdaptater.RecyclerItemClickListener {

    private var favorisList : MutableMap<String, Favori> = mutableMapOf()
    private lateinit var fAdapter: FavorisAdaptater
    //private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var currentView: View


    private var currentSong: Song? = null

    private var youTubePlayer: YouTubePlayer? = null
//    private lateinit var ensembleVideo: ConstraintLayout
//    private lateinit var btSupprimer : Button
//    private lateinit var btPartage : Button
    private lateinit var textePasDeFavori : TextView
    private lateinit var loading : ProgressBar

    private lateinit var viewModelFavori : FavorisViewModel
    private lateinit var recyclerView : RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        val factory = ViewModelFactory(AppWakeUp.repository)
        viewModelFavori = ViewModelProvider(this, factory).get(FavorisViewModel::class.java)
        viewModelFavori.getFavoris()

        //Initialisation des vues-------------------------------------------------------------
        currentView = inflater.inflate(R.layout.fragment_favori, container, false)
        fAdapter= FavorisAdaptater(requireContext(), favorisList, this)
        requireActivity().title = "Favoris"

        textePasDeFavori = currentView.findViewById(R.id.textPasFavori)
        loading = currentView.findViewById(R.id.pb_main_loader)

        viewModelFavori.getFavoriVideosLiveData().observe(requireActivity(), {
            if(it.error==null) {
                if(it.favoriList.isEmpty()){
                    favorisList.clear()
                    fAdapter.notifyDataSetChanged()
                    textePasDeFavori.visibility = View.VISIBLE
                } else {
                    favorisList.clear()
                    favorisList.putAll(it.favoriList)
                    fAdapter.notifyDataSetChanged()
                    textePasDeFavori.visibility = View.GONE
                }
            }
            loading.visibility = View.GONE
        })

        recyclerView = currentView.findViewById(R.id.recycler_list_video)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = fAdapter
        //--------------------------------------------------------------------------------------------------------

        //Initialisation du YoutubePlayer----------------------------------------------------------
    /*    youTubePlayerView = currentView.findViewById(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView)
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                this@FavorisFragment.youTubePlayer = youTubePlayer
                viewModelFavori.getCurrentSong().observe (requireActivity(), androidx.lifecycle.Observer { song ->
                    if (song != null) {
                        currentSongLength = song.duration
                        currentSong = song

                        if (this@FavorisFragment.youTubePlayer != null) {
                            this@FavorisFragment.youTubePlayer!!.loadVideo(song.id,song.lancement.toFloat())
                            this@FavorisFragment.youTubePlayer!!.setVolume(100)
                            youTubePlayerView.getPlayerUiController()
                                .setVideoTitle(song.title)
                        }
                    }
                })
            }
        })*/

        //-----------------------------------------------------------------------------------------

        //Gestion des differents boutons----------------------------------------
       /* ensembleVideo = currentView.findViewById(R.id.EnsembleVideo)
        btSupprimer = currentView.findViewById(R.id.list_video_supprimer_favori)
        btPartage = currentView.findViewById(R.id.list_video_partage)

        gestionBoutonSupprimer()
        gestionBoutonPartage()
        //-----------------------------------------------------------------------

        //Enlever le youyube player et le bouton au debut---
        ensembleVideo.visibility = View.GONE*/
        //--------------------------------------------------

        //--------------------------------------------------------------------------------------

        return currentView
    }



    //TODO créer classe abstraite ou créer classe BouttonPartage
/*
    //Gestion du clic sur le bouton suprimer
    private fun gestionBoutonSupprimer(){

        btSupprimer.setOnClickListener {
            if (currentSong != null) {
                viewModelFavori.deleteFavori(currentSong!!)
                if (ensembleVideo.visibility == View.VISIBLE) { // rendre le youtubeplayer et les bouton invisible
                    ensembleVideo.visibility = View.GONE
                }
            }
            else{
                Utility.createSimpleToast(
                    "Veuillez sélectionner une vidéo")
            }

        }
    }

    //Gestion du clic sur le bouton partage
    private fun gestionBoutonPartage(){

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
*/




    override fun onPlayListener(recherche: Favori, position: Int) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v="+recherche.idSong)
            )
        )
    }

    override fun onShareListener(fav: Favori, position: Int) {
        val action = FavorisFragmentDirections.actionFavorisFragmentToContactsListeShareFragment(fav.song!!)
        findNavController().navigate(action)
    }

    override fun onDeleteListener(favori: Favori, position: Int) {
       AlertDialog.Builder(requireContext()).setTitle("Supprimer favori")
            .setMessage("Voulez-vous supprimer \""+favori.song!!.title+"\" de vos favoris ?")
            .setPositiveButton("Supprimer") { _, _ ->
                viewModelFavori.deleteFavori(favori.song!!)
        }
            .setNeutralButton("Annuler") { _, _ -> }.create().show()
    }


}


