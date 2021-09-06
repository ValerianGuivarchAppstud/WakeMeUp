package com.vguivarc.wakemeup.domain.internal

import com.vguivarc.wakemeup.data.entity.ShareRingingRequest
import com.vguivarc.wakemeup.domain.external.entity.Contact
import com.vguivarc.wakemeup.domain.external.entity.ContactFacebook
import com.vguivarc.wakemeup.domain.external.entity.Ringing
import io.reactivex.Single

interface IContactProvider {
    suspend fun getContactList(): List<Contact>
    suspend fun saveContactStatus(profileContactId: String, isContact: Boolean): List<Contact>
    suspend fun shareRinging(shareRingingRequest: ShareRingingRequest): Ringing
    suspend fun getContactFacebookList(socialAuthToken: String?): List<ContactFacebook>
}
