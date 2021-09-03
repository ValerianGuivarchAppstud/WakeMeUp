package com.vguivarc.wakemeup.data.repository

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.vguivarc.wakemeup.data.api.ContactApi
import com.vguivarc.wakemeup.data.entity.ContactRequest
import com.vguivarc.wakemeup.data.entity.ShareRingingRequest
import com.vguivarc.wakemeup.domain.entity.Contact
import com.vguivarc.wakemeup.domain.entity.ContactFacebook
import com.vguivarc.wakemeup.domain.entity.Ringing
import com.vguivarc.wakemeup.domain.service.ContactService
import io.reactivex.Single
import retrofit2.Retrofit

class ContactRepository(retrofit: Retrofit) : ContactService {

    private val _contactFacebookList = MutableLiveData<ContactService.ContactFacebookListState>()

    private val contactApi = retrofit.create(ContactApi::class.java)

    override fun getContactList(): Single<List<Contact>> {
        return contactApi.getContacts().map {
            it.contacts
        }
    }
    override fun getContactFacebookList(socialAuthToken: String): Single<List<ContactFacebook>> {
        return contactApi.getContactFacebook(socialAuthToken)
    }

/*
    override fun getContactFacebookList(facebookId : String): LiveData<ContactService.ContactFacebookListState> {
        val request = GraphRequest.newMeRequest(
            AccessToken.getCurrentAccessToken()
        ) { _, _ ->
            GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/$facebookId/friends",
                null,
                HttpMethod.GET
            ) { response2 ->
                try {
                    val rawName = response2.jsonObject.getJSONArray("data")
                    val listIdFacebook = mutableListOf<String>()
                    for (i in 0 until rawName.length()) {
                        val jsonFacebook = rawName.getJSONObject(i)
                        listIdFacebook.add(jsonFacebook.getString("id"))
                    }
                    contactApi.getContactFacebook(listIdFacebook)
                    _contactFacebookList.value = ContactService.ContactFacebookListState(
                        list = contactApi.getContactFacebook(listIdFacebook).blockingGet().contacts
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    _contactFacebookList.value =
                        ContactService.ContactFacebookListState(error = e)
                }
            }.executeAsync()
        }

        val parameters = Bundle()
        parameters.putString("fields", "id,name,link,email,picture")
        request.parameters = parameters
        request.executeAsync()

        return _contactFacebookList
    }*/

    override fun saveContactStatus(
        profileContactId: String,
        isContact: Boolean
    ): Single<List<Contact>> {
        val contactRequest = ContactRequest(profileContactId, isContact)
        return contactApi.setContact(contactRequest)
    }

    override fun shareRinging(shareRingingRequest: ShareRingingRequest): Single<Ringing> {
        return contactApi.shareRinging(shareRingingRequest)
    }
}
