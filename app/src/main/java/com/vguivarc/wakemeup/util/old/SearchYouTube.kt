package com.vguivarc.wakemeup.util.old

/*
class SearchYouTube(private val videoSearchResult : MutableLiveData<Resource<List<Song>>>) : AsyncTask<String, Void, String>() {

    lateinit var  result: Resource<List<Song>>
    override fun doInBackground(vararg searchWord: String): String {
        videoSearchResult.postValue(Loading())
        result = try {
            val reponse =
                InternalSearchYouTube.search(
                    searchWord[0],
                    searchWord[1].toInt()
                ) ?: mutableListOf()
            val songList = mutableListOf<Song>()
            for(sr in reponse){
                songList.add(Song(sr))
            }
            Success<List<Song>>(songList)
        } catch (e: Exception) {
            Fail(e)
        }
        return ""
    }

    override fun onPostExecute(r: String?) {
        videoSearchResult.postValue(result)
    }


object InternalSearchYouTube {
    private const val DEVELOPER_KEY = YouTubeConfig.apiKey
    private const val APPLICATION_NAME =
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
}*/
