package com.vguivarc.wakemeup.domain.service

import com.vguivarc.wakemeup.data.entity.ShareRingingRequest
import com.vguivarc.wakemeup.domain.entity.Contact
import com.vguivarc.wakemeup.domain.entity.Favorite
import com.vguivarc.wakemeup.domain.entity.Ringing
import com.vguivarc.wakemeup.domain.entity.Song
import io.reactivex.Single

interface ContactService {
    fun getContactList(): Single<List<Contact>>
    fun saveContactStatus(profileContactId: String, isContact: Boolean): Single<List<Contact>>
    fun shareRinging(shareRingingRequest: ShareRingingRequest): Single<Ringing>
}
