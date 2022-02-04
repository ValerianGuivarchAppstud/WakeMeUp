package com.vguivarc.wakemeup.data.network

import com.vguivarc.wakemeup.data.entity.*
import com.vguivarc.wakemeup.domain.external.entity.Contact
import com.vguivarc.wakemeup.domain.external.entity.ContactFacebook
import com.vguivarc.wakemeup.domain.external.entity.Ringing
import com.vguivarc.wakemeup.domain.external.entity.UserProfile
import retrofit2.http.*

/**
 * Api to retrieve series information
 */
interface ContactApi {
    @GET("v1/contact/list")
    suspend fun getContacts(): List<Contact>

    @GET("v1/contact/facebook")
    suspend fun getContactFacebook(
        @Query("socialAuthToken") socialAuthToken: String?
    ): List<ContactFacebook>

    @GET("v1/contact/username")
    suspend fun searchByUsername(
        @Query("searchUsernameText") searchText: String
    ): SearchResultContactResponse


    @PUT("v1/contact/status")
    suspend fun setContact(
        @Body contactRequest: ContactRequest
    )

    @POST("v1/shareringing")
    suspend fun shareRinging(
        @Body commentRequest: SendRingingRequest
    ): Ringing
}
