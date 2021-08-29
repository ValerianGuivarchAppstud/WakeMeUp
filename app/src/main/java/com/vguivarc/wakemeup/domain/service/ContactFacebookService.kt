package com.vguivarc.wakemeup.domain.service

import com.vguivarc.wakemeup.data.entity.ShareRingingRequest
import com.vguivarc.wakemeup.domain.entity.Contact
import com.vguivarc.wakemeup.domain.entity.Favorite
import com.vguivarc.wakemeup.domain.entity.Ringing
import com.vguivarc.wakemeup.domain.entity.Song
import com.vguivarc.wakemeup.ui.contactlistfacebook.ContactFacebook
import io.reactivex.Single

interface ContactFacebookService {
    fun getContactFacebookList(facebookId: String): Single<List<ContactFacebook>>
}
