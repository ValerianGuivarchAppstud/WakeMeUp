package com.vguivarc.wakemeup.domain.external.entity

class AccountAlreadyExistsError : Throwable() {
    companion object {
        const val HTTP_ERROR_CODE = "ACCOUNT_ALREADY_CREATED"
    }
}
