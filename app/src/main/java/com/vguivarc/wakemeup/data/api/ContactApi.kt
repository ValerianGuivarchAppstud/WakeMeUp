package com.vguivarc.wakemeup.data.api

import com.vguivarc.wakemeup.data.entity.ContactsListResponse
import com.vguivarc.wakemeup.data.entity.ShareRingingRequest
import com.vguivarc.wakemeup.domain.entity.Ringing
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Api to retrieve series information
 */
interface ContactApi {
    @GET("v1/contacts")
    fun getContacts(): Single<ContactsListResponse>

    @POST("v1/shareringing")
    fun shareRinging(
        @Body commentRequest: ShareRingingRequest
    ): Single<Ringing>
}
