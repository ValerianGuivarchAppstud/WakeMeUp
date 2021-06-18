package com.vguivarc.wakemeup.data.repository

import com.hsuaxo.rxtube.RxTube
import com.hsuaxo.rxtube.YTResult
import com.vguivarc.wakemeup.domain.service.SongService
import com.vguivarc.wakemeup.ui.song.YouTubeConfig
import io.reactivex.Single

class SongRepository : SongService {

    companion object {
        private const val MUSIC_TO_LOAD: Int = 20
    }

    /*    private var lastSearch: String = ""
    private var searchYouTube: SearchYouTube? = null
    private val _videoSearchResult = MutableLiveData<Resource<List<Song>>>()
    val videoSearchResult: LiveData<Resource<List<Song>>> = _videoSearchResult*/
    val tube: RxTube = RxTube(YouTubeConfig.apiKey)

    override fun getSong(query: String): Single<YTResult> {
        return tube.searchVideos(query, MUSIC_TO_LOAD)
        /*lastSearch = query
        searchYouTube = SearchYouTube(_videoSearchResult)
        searchYouTube!!.execute(query, "" + 20)
        return Single.just(_videoSearchResult.value)*/
    }
}
