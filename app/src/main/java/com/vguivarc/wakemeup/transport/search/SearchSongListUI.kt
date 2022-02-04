package com.vguivarc.wakemeup.transport.search

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.entity.SearchSong
import com.vguivarc.wakemeup.domain.external.entity.Song
import com.vguivarc.wakemeup.transport.routeViewModel


@Composable
fun SearchSongListScreen(navController: NavController) {

    val searchSongListViewModel: SearchSongListViewModel =
        remember { navController.routeViewModel() }

    val state by searchSongListViewModel.container.stateFlow.collectAsState()

    val side by searchSongListViewModel.container.sideEffectFlow.collectAsState(initial = null)

    SearchSongListContent(
        searchSongListViewModel,
        state.searchSongList,
        state.searchText,
        state.currentSong,
        state.showBeforeSearch,
        state.showEmptyResult
    )
    side?.let {
        handleSideEffect(LocalContext.current, navController, it)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchSongListContent(
    searchSongListViewModel: SearchSongListViewModel?,
    searchSongs: List<SearchSong>,
    searchText: String,
    currentSong: SearchSong?,
    showBeforeSearch: Boolean,
    showEmptyResult: Boolean
) {
    Scaffold(
        content = {
            Column {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(32.dp, 8.dp)
                        .fillMaxWidth(),
                    value = searchText,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    onValueChange = { searchSongListViewModel?.setSearchText(it) },
                    shape = RoundedCornerShape(8.dp),
                    trailingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_baseline_search_24),
                            contentDescription = "Search",
                            colorFilter = ColorFilter.tint(
                                Color.Black
                            ),
                            modifier = Modifier.clickable(
                                onClick = { searchSongListViewModel?.getSearchedSongList() },
//                                indication = null
                            ),
                        )
                    },
                    placeholder = { Text("Search a new music") },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.LightGray,
                        disabledIndicatorColor = Color.LightGray,
                        unfocusedIndicatorColor = Color.LightGray,
                        backgroundColor = Color.Transparent,
                    )
                )
//                SearchBar(searchText, searchSongListViewModel, "hint")
                LazyColumn {
                    items(searchSongs) { song ->
                        SearchSongCard(searchSongListViewModel, song)
                    }
                }
            }
        }
    )
}


private fun handleSideEffect(
    context: Context,
    navController: NavController,
    sideEffect: SearchSongListSideEffect
) {
    when (sideEffect) {
        is SearchSongListSideEffect.Toast -> Toast.makeText(
            context,
            sideEffect.textResource,
            Toast.LENGTH_SHORT
        ).show()
    }
}

@Preview
@Composable
fun SearchSongListContentPreview() {
    SearchSongListContent(
        null,
        searchSongs = mutableListOf(
            SearchSong(Song("a", "favo"), false),
            SearchSong(Song("b", "nop"), true)
        ),
        "search",
        currentSong = null,
        showBeforeSearch = false,
        showEmptyResult = false
    )
}

@ExperimentalAnimationApi
@Composable
fun SearchBar(value:String, viewModel: SearchSongListViewModel?,  hint: String,
              endIcon: ImageVector? = Icons.Default.Cancel) {
    // we are creating a variable for
    // getting a value of our text field.

    Surface(
        shape = RoundedCornerShape(50),
      //  color = searchFieldColor
    ) {
        Box(
            modifier = Modifier
                .height(40.dp)
                .padding(start = 16.dp, end = 12.dp),
            contentAlignment = Alignment.CenterStart
        )
        {
            if (value.isEmpty()) {
                Text(
                    text = "Search...",
                    style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium)),
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    modifier = Modifier.weight(1f),
                    value = value,
                    onValueChange = { viewModel?.setSearchText(it) },
                    singleLine = true,
                  //  cursorColor = YourColor,
                )
                endIcon?.let {
                    AnimatedVisibility(
                        visible = value.isNotEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Image(
                            modifier = Modifier
                                .size(24.dp)
                                /*.clickable(
                                    onClick = { textValue = TextFieldValue() },
                                    indication = null
                                ),*/,
                            imageVector = endIcon,
                            contentDescription = ""
                            //colorFilter = iconColor
                        )
                    }
                }
            }
        }
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