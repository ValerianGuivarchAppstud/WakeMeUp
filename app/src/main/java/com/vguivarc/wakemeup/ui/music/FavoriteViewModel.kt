package com.vguivarc.wakemeup.ui.music

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vguivarc.wakemeup.base.*
import com.vguivarc.wakemeup.domain.entity.Favorite
import com.vguivarc.wakemeup.domain.service.FavoriteService
import com.vguivarc.wakemeup.util.applySchedulers
import io.reactivex.rxkotlin.addTo

class FavoriteViewModel(private val favoriteService: FavoriteService) : BaseViewModel() {

    private val _favoriteList = MutableLiveData<Resource<List<Favorite>>>()
    val favoriteList: LiveData<Resource<List<Favorite>>>
        get() = _favoriteList

    private val _favoriteStatus = MutableLiveData<Resource<Boolean>>()
    val favoriteStatus: LiveData<Resource<Boolean>>
        get() = _favoriteStatus

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

    fun saveFavoriteStatus(favorite: Favorite, isFavorite: Boolean) {
        favoriteService.saveFavoriteStatus(favorite, isFavorite)
            .applySchedulers()
            .doOnSubscribe { _favoriteStatus.postValue(Loading()) }
            .subscribe(
                {
                    _favoriteStatus.postValue(Success(isFavorite))
                },
                {
                    _favoriteStatus.postValue(Fail(it))
                }
            )
            .addTo(disposables)
    }

}
