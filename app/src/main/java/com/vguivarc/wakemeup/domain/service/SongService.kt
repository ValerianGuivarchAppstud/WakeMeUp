package com.vguivarc.wakemeup.domain.service

import com.hsuaxo.rxtube.YTResult
import io.reactivex.Single

interface SongService {
    fun getSong(searchText: String): Single<YTResult>
}
