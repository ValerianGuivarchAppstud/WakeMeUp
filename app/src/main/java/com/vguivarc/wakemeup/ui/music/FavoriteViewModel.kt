package com.vguivarc.wakemeup.ui.music

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.base.*
import com.vguivarc.wakemeup.data.repository.Repository
import com.vguivarc.wakemeup.domain.entity.Favorite
import com.vguivarc.wakemeup.domain.service.FavoriteService
import com.vguivarc.wakemeup.ui.song.Song
import com.vguivarc.wakemeup.util.AddFireBaseObjectResult
import com.vguivarc.wakemeup.util.applySchedulers
import io.reactivex.rxkotlin.addTo

class FavoriteViewModel(private val favoriteService: FavoriteService) : BaseViewModel() {

    private val _favoriteList = MutableLiveData<Resource<List<Favorite>>>()
    val favoriteList: LiveData<Resource<List<Favorite>>>
        get() = _favoriteList


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


    fun addFavorite(favorite: Favorite) {
        favoriteService.addFavorite(favorite)
    }

    fun deleteFavori(favorite: Favorite){
        favoriteService.removeFavorite(favorite)
    }


}