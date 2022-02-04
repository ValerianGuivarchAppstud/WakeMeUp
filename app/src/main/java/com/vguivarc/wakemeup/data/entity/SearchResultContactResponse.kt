package com.vguivarc.wakemeup.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vguivarc.wakemeup.domain.external.entity.Contact

@JsonClass(generateAdapter = true)
data class SearchResultContactResponse(
    @Json(name ="foundResult")
    val foundResult: Boolean = false,
    @Json(name = "idProfile")
    val idProfile: String = "",
    @Json(name = "username")
    val username: String = "",
    @Json(name = "pictureUrl")
    val pictureUrl: String? = null,
    @Json(name = "contact")
    val contact: Boolean = false
)
