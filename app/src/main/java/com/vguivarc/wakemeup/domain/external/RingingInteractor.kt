package com.vguivarc.wakemeup.domain.external

import com.vguivarc.wakemeup.domain.external.entity.Ringing
import com.vguivarc.wakemeup.domain.internal.IRingingProvider


class RingingInteractor(private val ringingProvider: IRingingProvider) {
    suspend fun getNextRinging() : Ringing? {
        return ringingProvider.getNextRinging()
    }
    suspend fun getWaitingRingingList() : List<Ringing> {
        return ringingProvider.getWaitingRingingList()
    }
    suspend fun stopAlarm(ringing: Ringing) {
        ringingProvider.listenRinging(ringing)
    }
    suspend fun snoozeAlarm(ringing: Ringing) {
        ringingProvider.listenRinging(ringing)
    }
}
