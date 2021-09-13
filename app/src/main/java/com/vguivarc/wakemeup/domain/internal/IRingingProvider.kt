package com.vguivarc.wakemeup.domain.internal

import com.vguivarc.wakemeup.domain.external.entity.Ringing
import com.vguivarc.wakemeup.domain.external.entity.UserProfile
import io.reactivex.Completable
import io.reactivex.Single


interface IRingingProvider {
    suspend fun getNextRinging(): Ringing?
    suspend fun getWaitingRingingList(): List<Ringing>
    suspend fun listenRinging(ringing: Ringing)
}
