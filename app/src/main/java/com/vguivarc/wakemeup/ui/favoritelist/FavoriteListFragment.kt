package com.vguivarc.wakemeup.ui.favoritelist

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.base.BaseLceFragment
import com.vguivarc.wakemeup.base.Fail
import com.vguivarc.wakemeup.base.Loading
import com.vguivarc.wakemeup.base.Success
import com.vguivarc.wakemeup.domain.entity.Favorite
import org.koin.android.ext.android.inject

class FavoriteListFragment :
    BaseLceFragment(R.layout.fragment_favori_list),
    FavoriteAdapter.RecyclerItemClickListener {

    private var favoriteList = mutableListOf<Favorite>()
    private lateinit var favoriteAdapter: FavoriteAdapter

    // private lateinit var youTubePlayerView: YouTubePlayerView
    private val viewModelFavorite: FavoriteViewModel by inject()
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteAdapter = FavoriteAdapter(favoriteList, this)

        viewModelFavorite.getFavoriteList()
        viewModelFavorite.favoriteList.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Loading -> showLoading()
                    is Success -> {
                        val favoriteList = it.data ?: emptyList()
                        refreshUI(favoriteList)
                        if (favoriteList.isEmpty()) showEmptyView() else showContent()
                    }
                    is Fail -> showError()
                }
            }
        )
        view.findViewById<FloatingActionButton>(R.id.button_add_favorite).setOnClickListener {
            findNavController().navigate(
                R.id.searchVideoFragment
            )
        }
    }

    private fun refreshUI(favoriteList: List<Favorite>) {
        favoriteAdapter.setFavoriteList(favoriteList)
    }

    // --------------------------------------------------------------------------------------------------------

    // Initialisation du YoutubePlayer----------------------------------------------------------
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

    // -----------------------------------------------------------------------------------------

    // Gestion des differents boutons----------------------------------------
    /* ensembleVideo = currentView.findViewById(R.id.EnsembleVideo)
     btSupprimer = currentView.findViewById(R.id.list_video_supprimer_favori)
     btPartage = currentView.findViewById(R.id.list_video_partage)

     gestionBoutonSupprimer()
     gestionBoutonPartage()
     //-----------------------------------------------------------------------

     //Enlever le youyube player et le bouton au debut---
     ensembleVideo.visibility = View.GONE*/
    // --------------------------------------------------

    // --------------------------------------------------------------------------------------

    // TODO créer classe abstraite ou créer classe BouttonPartage
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

    override fun onPlayListener(recherche: Favorite, position: Int) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + recherche.song!!.id)
            )
        )
    }

    override fun onShareListener(recherche: Favorite, position: Int) {
        /*  val action =
              com.vguivarc.wakemeup.song.favori.FavorisFragmentDirections.actionFavorisFragmentToContactsListeShareFragment(
                  recherche.song!!
              )
          findNavController().navigate(action)*/
    }

    override fun onDeleteListener(recherche: Favorite, position: Int) {
        AlertDialog.Builder(requireContext()).setTitle("Supprimer favori")
            .setMessage("Voulez-vous supprimer \"" + recherche.song!!.title + "\" de vos favoris ?")
            .setPositiveButton("Supprimer") { _, _ ->
                viewModelFavorite.saveFavoriteStatus(recherche, false)
            }
            .setNeutralButton("Annuler") { _, _ -> }.create().show()
    }
}
