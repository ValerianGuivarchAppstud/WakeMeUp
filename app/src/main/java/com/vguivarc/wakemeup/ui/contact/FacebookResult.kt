package com.vguivarc.wakemeup.ui.contact

import com.vguivarc.wakemeup.domain.entity.UserProfile

data class FacebookResult(
    val friendList: MutableMap<String, UserProfile> = mutableMapOf(),
    val error: Exception? = null
)
