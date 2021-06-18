package com.vguivarc.wakemeup.domain.service

import androidx.lifecycle.LiveData
import com.hsuaxo.rxtube.YTResult
import com.vguivarc.wakemeup.base.Resource
import com.vguivarc.wakemeup.domain.entity.Alarm
import com.vguivarc.wakemeup.ui.search.VideoSearchResult
import com.vguivarc.wakemeup.ui.song.Song
import io.reactivex.Single

interface SongService {
    fun getSong(searchText: String): Single<YTResult>

}