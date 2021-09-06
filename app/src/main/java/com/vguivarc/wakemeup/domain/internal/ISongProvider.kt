package com.vguivarc.wakemeup.domain.internal

import com.hsuaxo.rxtube.YTResult
import io.reactivex.Single

interface ISongProvider {
    suspend fun getSong(searchText: String): YTResult
}
