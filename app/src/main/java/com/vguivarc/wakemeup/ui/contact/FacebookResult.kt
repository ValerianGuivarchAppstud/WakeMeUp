package com.vguivarc.wakemeup.ui.contact

import com.vguivarc.wakemeup.domain.entity.UserModel

data class FacebookResult(
    val friendList: MutableMap<String, UserModel> = mutableMapOf(),
    val error: Exception? = null
)
