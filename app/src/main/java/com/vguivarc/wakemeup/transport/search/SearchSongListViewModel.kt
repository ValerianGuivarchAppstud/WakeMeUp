package com.vguivarc.wakemeup.transport.search

import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.domain.external.FavoriteInteractor
import com.vguivarc.wakemeup.domain.external.SessionInteractor
import com.vguivarc.wakemeup.domain.external.SongInteractor
import com.vguivarc.wakemeup.domain.external.entity.SearchSong
import com.vguivarc.wakemeup.util.toSong
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber


class SearchSongListViewModel(
    private val songInteractor: SongInteractor,
    private val favoriteInteractor: FavoriteInteractor,
    private val sessionInteractor: SessionInteractor
) :
    ContainerHost<SearchSongListState, SearchSongListSideEffect>, ViewModel() {

    override val container =
        container<SearchSongListState, SearchSongListSideEffect>(
            SearchSongListState(),
            onCreate = ::onCreate
        )

    private fun onCreate(initialState: SearchSongListState) {
    }



    fun getSearchedSongList(searchText: String) = intent {
        reduce {
            state.copy(isLoading = true)
        }

        try {
            val favoriteList = favoriteInteractor.getFavoriteList()
            val songList = songInteractor.getSong(searchText)
            val list= songList.items.map { song ->
                SearchSong(song.toSong(),favoriteList.any { it.song.id == song.id() })
            }

            reduce {
                state.copy(searchSongList = list, isLoading = false)
            }

        } catch (exception: Exception) {
            Timber.e(exception)

            reduce {
                state.copy(searchSongList = emptyList(), isLoading = false)
            }

//            postSideEffect(ContactFacebookListSideEffect.Toast(R.string.general_error))
        }
    }
    /*
    fun getSearchedSongList(searchText: String) {
        if(sessionService.isConnected()) {
            favoriteService.getFavoriteList()
                .applySchedulers()
                .subscribe(
                    { favoriteList ->
                        songService.getSong(searchText)
                            .applySchedulers()
                            .doOnSubscribe { _searchSongList.postValue(Loading()) }
                            .subscribe(
                                { ytResult ->
                                        val list= ytResult.items.map { song ->
                                            SearchSong(song.toSong(),favoriteList.any { it.song.id == song.id() })
                                        }
                                    _searchSongList.postValue(Success(list))
                                },
                                {
                                    _searchSongList.postValue(Fail(it))
                                }
                            )
                            .addTo(disposables)
                    },
                    {
                        _searchSongList.postValue(Fail(it))
                    }
                )
                .addTo(disposables)
        } else {
            songService.getSong(searchText)
                .applySchedulers()
                .doOnSubscribe { _searchSongList.postValue(Loading()) }
                .subscribe(
                    {
                        _searchSongList.postValue(Success(
                            it.items.map { ytContent -> SearchSong(ytContent.toSong(), false) }
                        ))
                    },
                    {
                        _searchSongList.postValue(Fail(it))
                    }
                )
                .addTo(disposables)
        }
    }

    fun updateFavorite(song: Song, isFavorite: Boolean) {
        favoriteService.saveFavoriteStatus(song, isFavorite)
            .applySchedulers()
            .subscribe(
                {
                    _searchSongList.value?.data?.filter { it.song.id == song.id }?.get(0)?.isFavorite = isFavorite
                    _searchSongList.postValue(Success(_searchSongList.value?.data))
                },
                {
                    _searchSongList.postValue(Fail(it))
                }
            )
            .addTo(disposables)
    }

    fun setCurrentSong(position: Int) {
        _currentSong.value = _searchSongList.value?.data?.get(position)
    }
    */

    /*

    private val currentSong = MutableLiveData<Song>()
    fun getCurrentSong() : LiveData<Song> = currentSong

    fun setCurrentSong(index: Int) {
        currentSong.value= rechercheVideosState.value!!.searchList[index]
    }

    private val rechercheVideosState = MediatorLiveData<VideoSearchResult>()
    fun getRechercheVideosLiveData(): LiveData<VideoSearchResult> = rechercheVideosState

    private val historiqueSearch = MediatorLiveData<List<String>>()
    private var sortedType : SortedListType = SortedListType.Date

    init {
        rechercheVideosState.addSource(repo.getVideoSearchResult()) { newVideoResults ->
            rechercheVideosState.value = newVideoResults
        }
        historiqueSearch.addSource(repo.getVideoSearchHistorique()) { newSearch ->
            historiqueSearch.value = when(sortedType){
                SortedListType.Date -> {
                    val sortedAlphaList = sortedSetOf<String>()
                    sortedAlphaList.addAll(newSearch.keys)
                    sortedAlphaList.toList()
                }
                SortedListType.Alphabetique -> {
                    val copyKeyMapInverse = mutableMapOf<Long, String>()
                    for( kv in newSearch.entries ) {
                        copyKeyMapInverse[kv.value] = kv.key
                    }
                    val sortedDateListKey = sortedSetOf<Long>()
                    sortedDateListKey.addAll(copyKeyMapInverse.keys)

                    val sortedDateList = mutableListOf<String>()
                    for(d in sortedDateListKey){
                        sortedDateList.add(copyKeyMapInverse[d]!!)
                    }
                    sortedDateList
                }
            }


        }
    }

    fun searchVideos(query: String){
        if (query.isNotEmpty()) {
            repo.searchVideos(query, MUSIC_TO_LOAD)
        }
    }*/
}
