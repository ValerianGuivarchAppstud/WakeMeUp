package com.vguivarc.wakemeup.domain.external.entity

class FacebookMissingMailError : Throwable() {
    companion object {
        const val HTTP_ERROR_CODE = "FB_MISSING_MAIL"
    }
}
