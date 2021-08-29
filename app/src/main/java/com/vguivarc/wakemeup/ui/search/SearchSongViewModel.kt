package com.vguivarc.wakemeup.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vguivarc.wakemeup.base.*
import com.vguivarc.wakemeup.domain.entity.Song
import com.vguivarc.wakemeup.domain.service.FavoriteService
import com.vguivarc.wakemeup.domain.service.SessionService
import com.vguivarc.wakemeup.domain.service.SongService
import com.vguivarc.wakemeup.util.applySchedulers
import com.vguivarc.wakemeup.util.toSong
import io.reactivex.rxkotlin.addTo
import java.util.*

class SearchSongViewModel(private val songService: SongService, private val favoriteService: FavoriteService, private val sessionService : SessionService) : BaseViewModel() {

    private val _searchSongList = MutableLiveData<Resource<List<SearchSong>>>()
    val searchSongList: LiveData<Resource<List<SearchSong>>>
        get() = _searchSongList


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
                                    _searchSongList.postValue(Success(
                                        ytResult.items.map { song -> SearchSong(song.toSong(),
                                            favoriteList.any { it.id == song.id() }) }
                                    ))
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
