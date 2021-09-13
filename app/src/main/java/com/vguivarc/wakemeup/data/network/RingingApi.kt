package com.vguivarc.wakemeup.data.network

import com.vguivarc.wakemeup.data.entity.*
import com.vguivarc.wakemeup.data.interceptor.HEADER_AUTHORIZATION
import com.vguivarc.wakemeup.domain.external.entity.Contact
import com.vguivarc.wakemeup.domain.external.entity.Ringing
import com.vguivarc.wakemeup.domain.external.entity.UserProfile
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface RingingApi {

    @GET("v1/ringing/next")
    suspend fun getNextRinging(): NextRingingResponse

    @GET("v1/ringing/list/waiting")
    suspend fun getWaitingRingingList(): List<Ringing>

    @GET("v1/ringing/list/listened")
    suspend fun getListRingingListened(): List<Ringing>

    @POST("v1/ringing/listen")
    suspend fun listenRinging(
        @Body ringingId: String
    )

    @PUT("v1/ringing/send")
    suspend fun sendRinging(
        @Body sendRingingRequest: SendRingingRequest
    )
}
