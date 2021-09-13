package com.vguivarc.wakemeup.data.provider

import com.vguivarc.wakemeup.data.network.RingingApi
import com.vguivarc.wakemeup.domain.external.entity.Ringing
import com.vguivarc.wakemeup.domain.internal.IRingingProvider
import retrofit2.Retrofit

class RingingProvider(retrofit: Retrofit) :
    IRingingProvider {

    private val ringingApi = retrofit.create(RingingApi::class.java)

    override suspend fun getNextRinging(): Ringing? {
       return ringingApi.getNextRinging().nextRinging
    }

    override suspend fun getWaitingRingingList(): List<Ringing> {
        return ringingApi.getWaitingRingingList()
    }

    override suspend fun listenRinging(ringing: Ringing) {
        return ringingApi.listenRinging(ringing.id)
    }


}
