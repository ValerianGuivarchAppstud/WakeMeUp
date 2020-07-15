package com.vguivarc.wakemeup.song.search


import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.apache.ApacheHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory

import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchResult
import com.vguivarc.wakemeup.song.Song
import com.vguivarc.wakemeup.song.YouTubeConfig

import java.io.IOException
import java.security.GeneralSecurityException


class SearchYouTubeOneSong(val videoSearchResultLiveData : MutableLiveData<VideoSearchResult>) : AsyncTask<String, Void, String>() {

    lateinit var  result: VideoSearchResult
    override fun doInBackground(vararg searchWord: String): String {
         try {
             val reponse =
                 InternalSearchYouTube.search(
                     searchWord[0],
                     1
                 ) ?: mutableListOf()
             val songList = mutableListOf<Song>()
             for(sr in reponse){
                 songList.add(Song(sr))
             }
             result= VideoSearchResult(songList)
        } catch (e: Exception) {
             result= VideoSearchResult(error = e)
         }
        return ""
    }

    override fun onPostExecute(r: String?) {
        videoSearchResultLiveData.postValue(result)
    }


object InternalSearchYouTube {
    private val DEVELOPER_KEY = YouTubeConfig.apiKey
    private val APPLICATION_NAME =
        YouTubeConfig.appName
    private val JSON_FACTORY = JacksonFactory.getDefaultInstance()

    /**
     * Build and return an authorized API client service.
     *
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    private val service: YouTube
        @Throws(GeneralSecurityException::class, IOException::class)
        get() {
            val httpTransport = ApacheHttpTransport()
            return YouTube.Builder(
                httpTransport,
                JSON_FACTORY, null
            )
                .setApplicationName(APPLICATION_NAME)
                .build()
        }

    /**
     * Call function to create API service object. Define and
     * execute API request. Print API response.
     *
     * @throws GeneralSecurityException, IOException, GoogleJsonResponseException
     */
    @Throws(GeneralSecurityException::class, IOException::class, GoogleJsonResponseException::class)


    fun search(searchWord: String, maxResult: Int): MutableList<SearchResult>? {
        val youtubeService =
            service
        val request = youtubeService.search()
            .list("snippet").setType("video")

        val response = request.setKey(DEVELOPER_KEY)
            .setQ(searchWord).setMaxResults(maxResult.toLong())
            .execute()


        return response.items
    }
}
}