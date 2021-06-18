package com.vguivarc.wakemeup.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hsuaxo.rxtube.YTResult
import com.vguivarc.wakemeup.base.*
import com.vguivarc.wakemeup.domain.service.SongService
import com.vguivarc.wakemeup.util.applySchedulers
import io.reactivex.rxkotlin.addTo
import java.util.*

class SearchSongViewModel(private val songService: SongService) : BaseViewModel() {

    private val _songList = MutableLiveData<Resource<YTResult>>()
    val songList: LiveData<Resource<YTResult>>
        get() = _songList

    fun getFavoriteList(searchText: String) {
        songService.getSong(searchText)
            .applySchedulers()
            .doOnSubscribe { _songList.postValue(Loading()) }
            .subscribe(
                {
                    _songList.postValue(Success(it))
                },
                {
                    _songList.postValue(Fail(it))
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
