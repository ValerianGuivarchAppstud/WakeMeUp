package com.vguivarc.wakemeup.domain.external

import com.vguivarc.wakemeup.domain.external.entity.Ringing
import com.vguivarc.wakemeup.domain.internal.IAuthProvider
import com.vguivarc.wakemeup.domain.internal.IRingingProvider


class RingingInteractor(private val ringingProvider: IRingingProvider, private val authProvider: IAuthProvider) {
    suspend fun getNextRinging() : Ringing? {
        return if(authProvider.isUserConnected()) {
             ringingProvider.getNextRinging()
        } else {
            null
        }
    }
    suspend fun getWaitingRingingList() : List<Ringing> {
        return if(authProvider.isUserConnected()) {
            ringingProvider.getWaitingRingingList()
        } else {
            listOf()
        }
    }
    suspend fun stopAlarm(ringing: Ringing) {
        ringingProvider.listenRinging(ringing)
    }
    suspend fun snoozeAlarm(ringing: Ringing) {
        ringingProvider.listenRinging(ringing)
    }
}
