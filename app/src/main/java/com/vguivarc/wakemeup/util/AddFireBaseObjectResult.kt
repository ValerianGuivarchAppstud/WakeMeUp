package com.vguivarc.wakemeup.util

import com.google.api.services.youtube.model.SearchResult

data class AddFireBaseObjectResult(
        val refpushkey: String = "",
        val error: Exception? = null
)
