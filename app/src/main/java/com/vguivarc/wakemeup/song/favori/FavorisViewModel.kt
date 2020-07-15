package com.vguivarc.wakemeup.song.favori

import androidx.lifecycle.*
import com.vguivarc.wakemeup.repo.Repository
import com.vguivarc.wakemeup.song.Song
import com.vguivarc.wakemeup.util.AddFireBaseObjectResult

class FavorisViewModel(private val repo : Repository) : ViewModel() {

    private val favoriVideosState = MediatorLiveData<VideoFavoriResult>()
    fun getFavoriVideosLiveData(): LiveData<VideoFavoriResult> = favoriVideosState

    private val addFavoriState = MediatorLiveData<AddFireBaseObjectResult>()
    fun getAddFavoriStateLiveData(): LiveData<AddFireBaseObjectResult> = addFavoriState

    fun addFavori(song : Song){
        repo.addFavori(song)
    }

    fun deleteFavori(song: Song){
        repo.deleteFavori(song)
    }

    fun getFavoris() {
        //todo move à l'endroit où on se connect
        repo.getFavoris()
    }

    init {
        addFavoriState.addSource(repo.getFavoriStateAddResult()) { newFavoriResults ->
            addFavoriState.value = newFavoriResults
        }
        favoriVideosState.addSource(repo.getFavorisListLiveData()) { newFavoriList ->
            favoriVideosState.value = newFavoriList
        }
    }



}