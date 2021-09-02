package com.vguivarc.wakemeup.ui.search

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.base.*
import kotlinx.android.synthetic.main.search_video_fragment.*
import org.koin.android.ext.android.inject

class SearchSongFragment :
    BaseLceFragment(R.layout.search_video_fragment),
    SearchSongAdapter.SongItemClickListener {

    private lateinit var youTubePlayerView: YouTubePlayerView

    private lateinit var searchVideoAdapterSearch: SearchSongAdapter
    private val viewModelVideoSearch: SearchSongViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as BaseActivity).setSupportActionBar(toolbar)
        (activity as BaseActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        searchVideoAdapterSearch = SearchSongAdapter(requireContext(), this)
        val recyclerView = view.findViewById<RecyclerView>(R.id.contentView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = searchVideoAdapterSearch
        searchVideoAdapterSearch.notifyDataSetChanged()

        button_valid_search_song.setOnClickListener {
            val imm = button_valid_search_song.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            viewModelVideoSearch.getSearchedSongList(search.text.toString())
        }

        viewModelVideoSearch.searchSongList.observe(
            viewLifecycleOwner,
            {
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
                viewModelVideoSearch.currentSong.observe(
                    viewLifecycleOwner,
                    {
                        it?.let {
                            searchVideoAdapterSearch.selectedSong = it
                            searchVideoAdapterSearch.notifyDataSetChanged()
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

    override fun onSongPictureClickListener(position: Int) {
        searchVideoAdapterSearch.notifyItemChanged(position)
        viewModelVideoSearch.setCurrentSong(position)
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
