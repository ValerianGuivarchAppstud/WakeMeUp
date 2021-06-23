package com.vguivarc.wakemeup.data.repository

import com.vguivarc.wakemeup.data.api.ContactApi
import com.vguivarc.wakemeup.data.entity.ShareRingingRequest
import com.vguivarc.wakemeup.domain.entity.Contact
import com.vguivarc.wakemeup.domain.entity.Ringing
import com.vguivarc.wakemeup.domain.service.ContactService
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Retrofit

class ContactRepository(retrofit: Retrofit) : ContactService {

    private val contactApi = retrofit.create(ContactApi::class.java)

    override fun getContact(): Single<List<Contact>> {
        return contactApi.getContacts().map {
            it.contacts
        }
    }

    override fun shareRinging(shareRingingRequest: ShareRingingRequest): Single<Ringing> {
        return contactApi.shareRinging(shareRingingRequest)
    }

}
