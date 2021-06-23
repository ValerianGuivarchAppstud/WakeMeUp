package com.vguivarc.wakemeup.domain.service

import com.vguivarc.wakemeup.data.entity.ShareRingingRequest
import com.vguivarc.wakemeup.domain.entity.Contact
import com.vguivarc.wakemeup.domain.entity.Favorite
import com.vguivarc.wakemeup.domain.entity.Ringing
import io.reactivex.Completable
import io.reactivex.Single

interface ContactService {
    fun getContact(): Single<List<Contact>>
    fun shareRinging(shareRingingRequest: ShareRingingRequest): Single<Ringing>
}
