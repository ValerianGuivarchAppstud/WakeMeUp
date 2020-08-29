package com.vguivarc.wakemeup.facebook

import com.vguivarc.wakemeup.connect.UserModel

data class FacebookResult(
    val friendList: MutableMap<String, UserModel> = mutableMapOf(),
    val error: Exception? = null
)
