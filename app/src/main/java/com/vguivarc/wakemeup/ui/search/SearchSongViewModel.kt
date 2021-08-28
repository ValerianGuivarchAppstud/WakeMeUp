package com.vguivarc.wakemeup.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vguivarc.wakemeup.base.*
import com.vguivarc.wakemeup.domain.entity.Song
import com.vguivarc.wakemeup.domain.service.FavoriteService
import com.vguivarc.wakemeup.domain.service.SongService
import com.vguivarc.wakemeup.util.applySchedulers
import io.reactivex.rxkotlin.addTo
import java.util.*

class SearchSongViewModel(private val songService: SongService, private val favoriteService: FavoriteService) : BaseViewModel() {

    private val _searchSongList = MutableLiveData<Resource<List<SearchSong>>>()
    val searchSongList: LiveData<Resource<List<SearchSong>>>
        get() = _searchSongList


    fun getSearchedSongList(searchText: String, isConnected: Boolean) {
        if(isConnected) {
            favoriteService.getFavoriteList()
                .applySchedulers()
                .subscribe(
                    {
                        _favoriteList.postValue(Success(it))
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
                            it.items.map { song -> SearchSong(song, false) }
                        ))
                    },
                    {
                        _searchSongList.postValue(Fail(it))
                    }
                )
                .addTo(disposables)
        }
    }

    fun getFavoriteList() {
        favoriteService.getFavoriteList()
            .applySchedulers()
            .doOnSubscribe { _favoriteList.postValue(Loading()) }
            .subscribe(
                {
                    _favoriteList.postValue(Success(it))
                },
                {
                    _favoriteList.postValue(Fail(it))
                }
            )
            .addTo(disposables)
    }

    fun addFavorite(song: Song) {
        favoriteService.saveFavoriteStatus(song, true)
            .applySchedulers()
            .subscribe(
                {
                    _favoriteList.postValue(Success(it))
                },
                {
                    _favoriteList.postValue(Fail(it))
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
