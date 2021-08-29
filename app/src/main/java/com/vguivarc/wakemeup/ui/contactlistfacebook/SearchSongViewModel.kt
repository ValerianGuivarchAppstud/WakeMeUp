package com.vguivarc.wakemeup.ui.contactlistfacebook

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

class ContactFacebookViewModel(private val songService: SongService, private val favoriteService: FavoriteService, private val sessionService : SessionService) : BaseViewModel() {

    private val _contactFacebookList = MutableLiveData<Resource<List<ContactFacebook>>>()
    val contactFacebookList: LiveData<Resource<List<ContactFacebook>>>
        get() = _contactFacebookList

/*
    fun getSearchedSongList(searchText: String) {
        if(sessionService.isConnected()) {
            favoriteService.getFavoriteList()
                .applySchedulers()
                .subscribe(
                    { favoriteList ->
                        songService.getSong(searchText)
                            .applySchedulers()
                            .doOnSubscribe { _contactFacebookList.postValue(Loading()) }
                            .subscribe(
                                { ytResult ->
                                    _contactFacebookList.postValue(Success(
                                        ytResult.items.map { song -> ContactFacebook(song.toSong(),
                                            favoriteList.any { it.id == song.id() }) }
                                    ))
                                },
                                {
                                    _contactFacebookList.postValue(Fail(it))
                                }
                            )
                            .addTo(disposables)
                    },
                    {
                        _contactFacebookList.postValue(Fail(it))
                    }
                )
                .addTo(disposables)
        } else {
            songService.getSong(searchText)
                .applySchedulers()
                .doOnSubscribe { _contactFacebookList.postValue(Loading()) }
                .subscribe(
                    {
                        _contactFacebookList.postValue(Success(
                            it.items.map { ytContent -> ContactFacebook(ytContent.toSong(), false) }
                        ))
                    },
                    {
                        _contactFacebookList.postValue(Fail(it))
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
                    _contactFacebookList.value?.data?.filter { it.song.id == song.id }?.get(0)?.isFavorite = isFavorite
                    _contactFacebookList.postValue(Success(_contactFacebookList.value?.data))
                },
                {
                    _contactFacebookList.postValue(Fail(it))
                }
            )
            .addTo(disposables)
    }
    */
}
