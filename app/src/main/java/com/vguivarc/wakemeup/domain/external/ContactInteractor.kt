package com.vguivarc.wakemeup.domain.external

import com.vguivarc.wakemeup.data.entity.ShareRingingRequest
import com.vguivarc.wakemeup.domain.external.entity.Contact
import com.vguivarc.wakemeup.domain.external.entity.ContactFacebook
import com.vguivarc.wakemeup.domain.external.entity.Ringing
import com.vguivarc.wakemeup.domain.internal.IContactProvider

class ContactInteractor(private val contactProvider: IContactProvider) {
    suspend fun getContactList(): List<Contact> {
        return contactProvider.getContactList()
    }

    suspend fun getContactFacebookList(socialAuthToken: String?): List<ContactFacebook> {
        return contactProvider.getContactFacebookList(socialAuthToken)
    }

    suspend fun saveContactStatus(profileContactId: String, isContact: Boolean): List<Contact> {
        return contactProvider.saveContactStatus(profileContactId, isContact)
    }
    suspend fun shareRinging(shareRingingRequest: ShareRingingRequest): Ringing {
        return contactProvider.shareRinging(shareRingingRequest)
    }
}
