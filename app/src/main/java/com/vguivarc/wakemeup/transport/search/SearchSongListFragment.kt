package com.vguivarc.wakemeup.transport.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.entity.Contact
import com.vguivarc.wakemeup.domain.external.entity.SearchSong
import com.vguivarc.wakemeup.domain.external.entity.Song
import com.vguivarc.wakemeup.transport.ui.theme.WakeMeUpTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber



class SearchSongListFragment : Fragment() {

    private val viewModel by viewModel<SearchSongListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WakeMeUpTheme {
                    SearchSongListScreen(findNavController(), viewModel)
                }
            }
        }
    }



    private fun handleSideEffect(navController: NavController, sideEffect: SearchSongListSideEffect) {
        when (sideEffect) {
            is SearchSongListSideEffect.Toast -> Toast.makeText(
                context,
                sideEffect.textResource,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @Composable
    fun SearchSongListScreen(navController: NavController, searchSongListViewModel: SearchSongListViewModel) {
        val state by searchSongListViewModel.container.stateFlow.collectAsState()

        val side by searchSongListViewModel.container.sideEffectFlow.collectAsState(initial = null)

        SearchSongListContent(
            state.searchSongList,
            state.currentSong,
            state.showBeforeSearch,
            state.showEmptyResult
        )
        side?.let {
            handleSideEffect(navController, it)
        }
    }

    @Composable
    fun SearchSongListContent(
        searchSongs: List<SearchSong>,
        currentSong: SearchSong?,
        showBeforeSearch: Boolean,
        showEmptyResult: Boolean
    ) {
        Scaffold(
            content = {
                Column {
                    LazyColumn {
                        items(searchSongs) { song ->
                            SearchSongCard(song)
                        }
                    }
                }
            }
        )
    }

    @Preview
    @Composable
    fun SearchSongListContentPreview() {
        SearchSongListContent(
            searchSongs = mutableListOf(
                SearchSong(Song("a", "favo"), false),
                SearchSong(Song("b", "nop"), true)
            ),
            null,
            false,
            false
        )
    }


}

/*
class SearchSongListFragment : Fragment(R.layout.fragment_search_song_list),
    SearchSongListAdapter.SongItemClickListener {

    private val viewModel by viewModel<SearchSongListViewModel>()

    private var _binding: FragmentSearchSongListBinding? = null

    private var youtubePlayer: YouTubePlayer? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchSongListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonValidSearchSong.setOnClickListener {
            val imm = binding.buttonValidSearchSong.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            viewModel.getSearchedSongList(binding.search.text.toString())
        }

        lifecycle.addObserver(binding.youtubePlayerView)
        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(yt: YouTubePlayer) {
                youtubePlayer = yt
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observe(this, state = ::render, sideEffect = ::handleSideEffect)
    }

    private fun render(state: SearchSongListState) {
        Timber.d("render $state")
        binding.loader.isVisible = state.isLoading
        binding.beforeSearchView.isVisible = state.showBeforeSearch
        binding.emptyView.isVisible = state.showEmptyResult
        binding.searchSongList.isVisible = !state.isLoading
        if (binding.searchSongList.adapter == null) {
            binding.searchSongList.adapter = SearchSongListAdapter(state.searchSongList, this)
            binding.searchSongList.layoutManager = LinearLayoutManager(context)
        } else {
            (binding.searchSongList.adapter as? SearchSongListAdapter)?.updateData(state.searchSongList)
        }
        state.currentSong?.let {
            (binding.searchSongList.adapter as? SearchSongListAdapter)?.selectedSong(it)
            youtubePlayer?.let { youtubePlayer ->
                binding.youtubePlayerView.visibility = View.VISIBLE
                youtubePlayer.loadVideo(
                    it.song.id,
                    0f
                )
                youtubePlayer.setVolume(100)
                binding.youtubePlayerView.getPlayerUiController()
                    .setVideoTitle(it.song.title)
            }
        } ?: run {
            binding.youtubePlayerView.visibility = View.GONE
        }
    }

    private fun handleSideEffect(sideEffect: SearchSongListSideEffect) {
        when (sideEffect) {
            is SearchSongListSideEffect.Toast -> Toast.makeText(
                context,
                sideEffect.textResource,
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun onSongPictureClickListener(position: Int) {
        val searchedSong = (binding.searchSongList.adapter as? SearchSongListAdapter)?.getSongWithPosition(position)
        searchedSong?.let { searchedSong ->
            viewModel.selectSong(searchedSong)
        }
    }

    override fun onSongFavoriteClickListener(position: Int) {
        val searchedSong = (binding.searchSongList.adapter as? SearchSongListAdapter)?.getSongWithPosition(position)
        searchedSong?.let { searchedSong ->
            viewModel.updateFavorite(searchedSong.song, searchedSong.isFavorite.not())
        }
    }

    override fun onSongShareClickListener(position: Int) {
        TODO("Not yet implemented")
    }
}

*/
/*
-------------------------------------------
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
*/