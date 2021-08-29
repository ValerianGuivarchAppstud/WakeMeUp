package com.vguivarc.wakemeup.data.api

import com.vguivarc.wakemeup.data.entity.ContactRequest
import com.vguivarc.wakemeup.data.entity.ContactsListResponse
import com.vguivarc.wakemeup.data.entity.FavoriteRequest
import com.vguivarc.wakemeup.data.entity.ShareRingingRequest
import com.vguivarc.wakemeup.domain.entity.Contact
import com.vguivarc.wakemeup.domain.entity.Favorite
import com.vguivarc.wakemeup.domain.entity.Ringing
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

/**
 * Api to retrieve series information
 */
interface ContactApi {
    @GET("v1/contacts/list")
    fun getContacts(): Single<ContactsListResponse>


    @PUT("v1/contact/status")
    fun setContact(
        @Body contactRequest: ContactRequest
    ): Single<List<Contact>>

    @POST("v1/shareringing")
    fun shareRinging(
        @Body commentRequest: ShareRingingRequest
    ): Single<Ringing>
}
