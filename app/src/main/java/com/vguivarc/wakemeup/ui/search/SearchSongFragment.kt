package com.vguivarc.wakemeup.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hsuaxo.rxtube.YTResult
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.base.BaseLceFragment
import com.vguivarc.wakemeup.base.Fail
import com.vguivarc.wakemeup.base.Loading
import com.vguivarc.wakemeup.base.Success
import com.vguivarc.wakemeup.domain.entity.Favorite
import com.vguivarc.wakemeup.ui.music.FavoriteViewModel
import com.vguivarc.wakemeup.domain.entity.Song
import com.vguivarc.wakemeup.ui.song.SongAdapter
import kotlinx.android.synthetic.main.search_video_fragment.*
import org.koin.android.ext.android.inject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class SearchSongFragment :
    BaseLceFragment(R.layout.search_video_fragment),
    SongAdapter.SongItemClickListener {

    private lateinit var youTubePlayerView: YouTubePlayerView

    private var currentIndex: Int = 0
    private var currentSong = MutableLiveData<Song?>()

    // validé
    private val searchVideosList = mutableListOf<Song>()
    private lateinit var searchVideoAdapter: SongAdapter
    private val viewModelVideo: SongViewModel by inject()
    private val viewModelFavorite: FavoriteViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchVideoAdapter = SongAdapter(requireContext(), searchVideosList, this)
        val recyclerView = view.findViewById<RecyclerView>(R.id.contentView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = searchVideoAdapter
        searchVideoAdapter.notifyDataSetChanged()

        button_valid_search_song.setOnClickListener {
            viewModelVideo.getFavoriteList(search.text.toString())
        }

        viewModelVideo.songList.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Loading -> showLoading()
                    is Success -> {
                        val ytResult: YTResult = it.data ?: YTResult()
                        refreshUI(ytResult)
                        if (ytResult.items.isEmpty()) showEmptyView() else showContent()
                    }
                    is Fail -> showError()
                }
            }
        )

        // Si on est a la fin du recycler view---------------------------------------------------
        /*  recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
              override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                  super.onScrollStateChanged(recyclerView, newState)
                  if (!recyclerView.canScrollVertically(1)) {
                      viewModelSearchVideo.addSearchVideo()
                  }
              }
          })*/
        // ---------------------------------------------------------------------------------------

        // Initialisation du YoutubePlayer----------------------------------------------------------
        youTubePlayerView = view.findViewById(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView)
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                currentSong.observe(
                    viewLifecycleOwner,
                    Observer {
                        it?.let {
                            youTubePlayerView.visibility = View.VISIBLE
                            youTubePlayer.loadVideo(
                                it.id,
                                0f
                            )
                            youTubePlayer.setVolume(100)
                            youTubePlayerView.getPlayerUiController()
                                .setVideoTitle(it.title)
                        } ?: run {
                            youTubePlayerView.visibility = View.GONE
                        }
                    }
                )
            }
        }
        )
    }

    // --------------------------------------------------

    // Gestion du clic sur le bouton partage
            /* private fun gestionBoutonPartage(){
                btPartage.setOnClickListener {
                     if (AndroidApplication.repository.getCurrentUser()==null) {
                     //TODO remplacer par alertdialog (en commentaire) pour envoyer vers le fragment de connection
                         Utility.createSimpleToast(AndroidApplication.appContext.resources.getString(R.string.vous_netes_pas_connecte))
                     } else {
                         if (currentSong != null) {
         /*                    val action =
                                 com.vguivarc.wakemeup.song.search.RechercheVideoFragmentDirections.actionRechercheVideoFragmentToContactsListeShareFragment(
                                     currentSong!!
                                 )
                             findNavController().navigate(action)*/
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
             }*/

// TODO historique marche pas, et inversion alphabétique et date ajout
    // TODO quand on descend ça marche pas
    // TODO remettre le dernier truc cherché dans la barre de recherche
    @SuppressLint("LogNotTimber")
    override fun onSongPictureClickListener(position: Int) {
        searchVideoAdapter.notifyItemChanged(searchVideoAdapter.selectedPosition)
        currentIndex = position
        searchVideoAdapter.selectedPosition = currentIndex
        searchVideoAdapter.notifyItemChanged(currentIndex)
        currentSong.value = searchVideoAdapter.getSongWithPosition(position)
        // //////viewModelSearchVideo.setCurrentSong(position)
    }

    private fun getNowTxt(): String {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date).toString()
    }
    override fun onSongFavoriteClickListener(position: Int) {
        viewModelFavorite.saveFavoriteStatus(
            Favorite(
                searchVideoAdapter.getSongWithPosition(position).id,
                getNowTxt(),
                searchVideoAdapter.getSongWithPosition(position)
            ),
            true
        )
    }

    override fun onSongShareClickListener(position: Int) {
        TODO("Not yet implemented")
    }

    private fun refreshUI(ytResult: YTResult) {
        searchVideoAdapter.setYTResult(ytResult)
    }
}
