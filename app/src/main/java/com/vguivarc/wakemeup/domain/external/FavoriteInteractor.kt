package com.vguivarc.wakemeup.domain.external

import com.vguivarc.wakemeup.domain.external.entity.Contact
import com.vguivarc.wakemeup.domain.external.entity.ContactFacebook
import com.vguivarc.wakemeup.domain.external.entity.Favorite
import com.vguivarc.wakemeup.domain.external.entity.Song
import com.vguivarc.wakemeup.domain.internal.IContactProvider
import com.vguivarc.wakemeup.domain.internal.IFavoriteProvider

class FavoriteInteractor(private val favoriteProvider: IFavoriteProvider) {
    suspend fun getFavoriteList(): List<Favorite> {
        return favoriteProvider.getFavoriteList()
    }
    suspend fun saveFavoriteStatus(song: Song, isFavorite: Boolean): List<Favorite> {
        return favoriteProvider.saveFavoriteStatus(song, isFavorite)
    }

}
