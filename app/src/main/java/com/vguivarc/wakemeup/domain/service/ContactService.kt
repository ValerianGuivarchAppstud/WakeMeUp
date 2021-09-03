package com.vguivarc.wakemeup.domain.service

import androidx.lifecycle.LiveData
import com.vguivarc.wakemeup.data.entity.ShareRingingRequest
import com.vguivarc.wakemeup.domain.entity.*
import io.reactivex.Single

interface ContactService {
    fun getContactList(): Single<List<Contact>>
    fun saveContactStatus(profileContactId: String, isContact: Boolean): Single<List<Contact>>
    fun shareRinging(shareRingingRequest: ShareRingingRequest): Single<Ringing>

    data class ContactFacebookListState(
        val list : List<ContactFacebook>? = null,
        val error : Exception? = null
    )

    fun getContactFacebookList(socialAuthToken: String): Single<List<ContactFacebook>>
}
