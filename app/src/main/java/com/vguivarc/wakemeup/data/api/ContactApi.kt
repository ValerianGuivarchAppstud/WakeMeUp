package com.vguivarc.wakemeup.data.api

import com.vguivarc.wakemeup.data.entity.*
import com.vguivarc.wakemeup.domain.entity.Contact
import com.vguivarc.wakemeup.domain.entity.ContactFacebook
import com.vguivarc.wakemeup.domain.entity.Ringing
import io.reactivex.Single
import retrofit2.http.*

/**
 * Api to retrieve series information
 */
interface ContactApi {
    @GET("v1/contacts/list")
    fun getContacts(): Single<ContactsListResponse>

    @GET("v1/contacts/facebook")
    fun getContactFacebook(
        @Query("social_auth_token") socialAuthToken: String
    ): Single<List<ContactFacebook>>


    @PUT("v1/contact/status")
    fun setContact(
        @Body contactRequest: ContactRequest
    ): Single<List<Contact>>

    @POST("v1/shareringing")
    fun shareRinging(
        @Body commentRequest: ShareRingingRequest
    ): Single<Ringing>
}
