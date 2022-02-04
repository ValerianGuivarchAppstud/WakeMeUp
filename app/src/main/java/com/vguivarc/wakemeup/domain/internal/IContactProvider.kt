package com.vguivarc.wakemeup.domain.internal

import com.vguivarc.wakemeup.data.entity.SearchResultContactResponse
import com.vguivarc.wakemeup.data.entity.SendRingingRequest
import com.vguivarc.wakemeup.domain.external.entity.Contact
import com.vguivarc.wakemeup.domain.external.entity.ContactFacebook
import com.vguivarc.wakemeup.domain.external.entity.Ringing
import com.vguivarc.wakemeup.domain.external.entity.UserProfile

interface IContactProvider {
    suspend fun getContactList(): List<Contact>
    suspend fun saveContactStatus(profileContactId: String, isContact: Boolean)
    suspend fun shareRinging(shareRingingRequest: SendRingingRequest): Ringing
    suspend fun getContactFacebookList(socialAuthToken: String?): List<ContactFacebook>
    suspend fun searchByUsername(searchText: String): SearchResultContactResponse
}
