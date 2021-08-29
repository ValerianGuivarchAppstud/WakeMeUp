package com.vguivarc.wakemeup.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.base.*
import kotlinx.android.synthetic.main.fragment_auth_email.*
import kotlinx.android.synthetic.main.search_video_fragment.*
import org.koin.android.ext.android.inject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class SearchSongFragment :
    BaseLceFragment(R.layout.search_video_fragment),
    SearchSongAdapter.SongItemClickListener {

    private lateinit var youTubePlayerView: YouTubePlayerView

    private var currentIndex: Int = 0
    private var currentSong = MutableLiveData<SearchSong?>()

    private val searchVideosList = mutableListOf<SearchSong>()
    private lateinit var searchVideoAdapterSearch: SearchSongAdapter
    private val viewModelVideoSearch: SearchSongViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as BaseActivity).setSupportActionBar(loginEmailToolbar)
        (activity as BaseActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        searchVideoAdapterSearch = SearchSongAdapter(requireContext(), searchVideosList, this)
        val recyclerView = view.findViewById<RecyclerView>(R.id.contentView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = searchVideoAdapterSearch
        searchVideoAdapterSearch.notifyDataSetChanged()

        button_valid_search_song.setOnClickListener {
            viewModelVideoSearch.getSearchedSongList(search.text.toString())
        }

        viewModelVideoSearch.searchSongList.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Loading -> showLoading()
                    is Success -> {
                        refreshUI(it.data ?: listOf())
                        if (it.data.isNullOrEmpty())
                            showEmptyView()
                        else
                            showContent()
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
                                it.song.id,
                                0f
                            )
                            youTubePlayer.setVolume(100)
                            youTubePlayerView.getPlayerUiController()
                                .setVideoTitle(it.song.title)
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
        searchVideoAdapterSearch.notifyItemChanged(searchVideoAdapterSearch.selectedPosition)
        currentIndex = position
        searchVideoAdapterSearch.selectedPosition = currentIndex
        searchVideoAdapterSearch.notifyItemChanged(currentIndex)
        currentSong.value = searchVideoAdapterSearch.getSongWithPosition(position)
        // //////viewModelSearchVideo.setCurrentSong(position)
    }

    private fun getNowTxt(): String {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date).toString()
    }
    override fun onSongFavoriteClickListener(position: Int) {
            viewModelVideoSearch.updateFavorite(
                searchVideoAdapterSearch.getSongWithPosition(position).song,
                searchVideoAdapterSearch.getSongWithPosition(position).isFavorite.not()
            )
    }

    override fun onSongShareClickListener(position: Int) {
        TODO("Not yet implemented")
    }

    private fun refreshUI(listSearchSong: List<SearchSong>) {
        searchVideoAdapterSearch.setListSearchSong(listSearchSong)
    }
}
