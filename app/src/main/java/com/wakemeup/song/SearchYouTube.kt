package com.wakemeup.song


import android.os.AsyncTask
import android.util.Log
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.apache.ApacheHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory

import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchResult

import java.io.IOException
import java.security.GeneralSecurityException


class SearchYouTube(private val mainActivity: VideoFragment) : AsyncTask<String, Void, String>() {


    private var exception: Exception? = null
    private var response: MutableList<SearchResult> = mutableListOf()

    override fun doInBackground(vararg searchWord: String): String {
        return try {
            response = InternalSearchYouTube.search(
                searchWord[0],
                searchWord[1].toInt()
            ) ?: mutableListOf()
            response.toString()


        } catch (e: Exception) {
            this.exception = e
            Log.e("FAIL", exception.toString())
            ""
        }
    }

    override fun onPostExecute(feed: String) {
        for (search in response) {
            mainActivity.updateAff(response)
        }
    }
}


object InternalSearchYouTube {
    private val DEVELOPER_KEY = YouTubeConfig.apiKey
    private val APPLICATION_NAME = YouTubeConfig.appName
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
        val youtubeService = service
        // Define and execute the API request
        /*        YouTube.Channels.List request = youtubeService.channels()
                .list("snippet,contentDetails,statistics");
        ChannelListResponse response = request.setKey(DEVELOPER_KEY)
                .setId("UC_x5XG1OV2P6uZZ5FSM9Ttw")
                .execute();
     */
        val request = youtubeService.search()
            .list("snippet").setType("video")

        val response = request.setKey(DEVELOPER_KEY)
            .setQ(searchWord).setMaxResults(maxResult.toLong())
            .execute()

        return response.items
    }
}