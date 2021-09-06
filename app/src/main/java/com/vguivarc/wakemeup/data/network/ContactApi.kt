package com.vguivarc.wakemeup.data.network

import com.vguivarc.wakemeup.data.entity.*
import com.vguivarc.wakemeup.domain.external.entity.Contact
import com.vguivarc.wakemeup.domain.external.entity.ContactFacebook
import com.vguivarc.wakemeup.domain.external.entity.Ringing
import io.reactivex.Single
import retrofit2.http.*

/**
 * Api to retrieve series information
 */
interface ContactApi {
    @GET("v1/contacts/list")
    suspend fun getContacts(): List<Contact>

    @GET("v1/contacts/facebook")
    suspend fun getContactFacebook(
        @Query("social_auth_token") socialAuthToken: String?
    ): List<ContactFacebook>


    @PUT("v1/contact/status")
    suspend fun setContact(
        @Body contactRequest: ContactRequest
    ): List<Contact>

    @POST("v1/shareringing")
    suspend fun shareRinging(
        @Body commentRequest: ShareRingingRequest
    ): Ringing
}
