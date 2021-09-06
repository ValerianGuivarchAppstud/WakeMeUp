package com.vguivarc.wakemeup.transport.contact

import com.vguivarc.wakemeup.domain.external.entity.UserProfile

data class FacebookResult(
    val friendList: MutableMap<String, UserProfile> = mutableMapOf(),
    val error: Exception? = null
)
