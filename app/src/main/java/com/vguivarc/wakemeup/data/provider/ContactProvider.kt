package com.vguivarc.wakemeup.data.provider

import com.vguivarc.wakemeup.data.network.ContactApi
import com.vguivarc.wakemeup.data.entity.ContactRequest
import com.vguivarc.wakemeup.data.entity.SendRingingRequest
import com.vguivarc.wakemeup.domain.external.entity.Contact
import com.vguivarc.wakemeup.domain.external.entity.ContactFacebook
import com.vguivarc.wakemeup.domain.external.entity.Ringing
import com.vguivarc.wakemeup.domain.internal.IContactProvider
import retrofit2.Retrofit

class ContactProvider(retrofit: Retrofit) : IContactProvider {

    private val contactApi = retrofit.create(ContactApi::class.java)

    override suspend fun getContactList(): List<Contact> {
        return contactApi.getContacts()
    }
    override suspend fun getContactFacebookList(socialAuthToken: String?): List<ContactFacebook> {
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

    override suspend fun saveContactStatus(
        profileContactId: String,
        isContact: Boolean
    ) {
        val contactRequest = ContactRequest(profileContactId, isContact)
        return contactApi.setContact(contactRequest)
    }

    override suspend fun shareRinging(shareRingingRequest: SendRingingRequest): Ringing {
        return contactApi.shareRinging(shareRingingRequest)
    }
}
