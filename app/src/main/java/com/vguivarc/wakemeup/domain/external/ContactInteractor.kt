package com.vguivarc.wakemeup.domain.external

import com.vguivarc.wakemeup.data.entity.SearchResultContactResponse
import com.vguivarc.wakemeup.data.entity.SendRingingRequest
import com.vguivarc.wakemeup.domain.external.entity.Contact
import com.vguivarc.wakemeup.domain.external.entity.ContactFacebook
import com.vguivarc.wakemeup.domain.external.entity.Ringing
import com.vguivarc.wakemeup.domain.external.entity.UserProfile
import com.vguivarc.wakemeup.domain.internal.IContactProvider

class ContactInteractor(private val contactProvider: IContactProvider) {
    suspend fun getContactList(): List<Contact> {
        return contactProvider.getContactList()
    }

    suspend fun searchByUsername(searchText: String): SearchResultContactResponse {
        return contactProvider.searchByUsername(searchText)
    }

    suspend fun getContactFacebookList(socialAuthToken: String?): List<ContactFacebook> {
        return contactProvider.getContactFacebookList(socialAuthToken)
    }

    suspend fun saveContactStatus(profileContactId: String, isContact: Boolean) {
        return contactProvider.saveContactStatus(profileContactId, isContact)
    }
    suspend fun shareRinging(shareRingingRequest: SendRingingRequest): Ringing {
        return contactProvider.shareRinging(shareRingingRequest)
    }
}
