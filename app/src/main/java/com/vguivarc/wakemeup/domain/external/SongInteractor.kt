package com.vguivarc.wakemeup.domain.external

import com.hsuaxo.rxtube.YTResult
import com.vguivarc.wakemeup.domain.external.entity.Contact
import com.vguivarc.wakemeup.domain.external.entity.ContactFacebook
import com.vguivarc.wakemeup.domain.external.entity.Favorite
import com.vguivarc.wakemeup.domain.external.entity.Song
import com.vguivarc.wakemeup.domain.internal.IContactProvider
import com.vguivarc.wakemeup.domain.internal.IFavoriteProvider
import com.vguivarc.wakemeup.domain.internal.ISongProvider

class SongInteractor(private val songProvider: ISongProvider) {
    suspend fun getSong(searchText: String): YTResult {
        return songProvider.getSong(searchText)
    }
}
