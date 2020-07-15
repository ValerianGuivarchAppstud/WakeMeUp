package com.vguivarc.wakemeup.song.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.repo.Repository
import com.vguivarc.wakemeup.song.Song
import com.vguivarc.wakemeup.util.SortedListType

class RechercheVideoViewModel(private val repo : Repository) : ViewModel() {

    private val MUSIC_TO_LOAD = 20

    private val currentSong = MutableLiveData<Song>()
    fun getCurrentSong() : LiveData<Song> = currentSong

    fun setCurrentSong(index: Int) {
        currentSong.value=rechercheVideosState.value!!.searchList.get(index)
    }

    private val rechercheVideosState = MediatorLiveData<VideoSearchResult>()
    fun getRechercheVideosLiveData(): LiveData<VideoSearchResult> = rechercheVideosState

    private val historiqueSearch = MediatorLiveData<List<String>>()
    var sortedType : SortedListType = SortedListType.date

    fun getSortedHistoriqueSearchLiveData(): LiveData<List<String>> = historiqueSearch

    fun changeSortedType(newSortedType : SortedListType){
        sortedType=newSortedType
        repo.notifyNewSortedType()
    }

    init {
        rechercheVideosState.addSource(repo.getVideoSearchResult()) { newVideoResults ->
            rechercheVideosState.value = newVideoResults
        }
        historiqueSearch.addSource(repo.getVideoSearchHistorique()) { newSearch ->
            historiqueSearch.value = when(sortedType){
                SortedListType.date -> {
                    val sortedAlphaList = sortedSetOf<String>()
                    sortedAlphaList.addAll(newSearch.keys)
                    sortedAlphaList.toList()
                }
                SortedListType.alphabetique -> {
                    val copyKeyMapInverse = mutableMapOf<Long, String>()
                    for( kv in newSearch.entries ) {
                        copyKeyMapInverse.put(kv.value, kv.key)
                    }
                    val sortedDateListKey = sortedSetOf<Long>()
                    sortedDateListKey.addAll(copyKeyMapInverse.keys)

                    val sortedDateList = mutableListOf<String>()
                    for(d in sortedDateListKey){
                        sortedDateList.add(copyKeyMapInverse.get(d)!!)
                    }
                    sortedDateList
                }
            }


        }
    }

    fun addSearchVideo(){
        repo.addSearchVideo(MUSIC_TO_LOAD)
    }

    fun searchVideos(query: String){
        if (query.isNotEmpty()) {
            repo.searchVideos(query, MUSIC_TO_LOAD)
        }
    }
}