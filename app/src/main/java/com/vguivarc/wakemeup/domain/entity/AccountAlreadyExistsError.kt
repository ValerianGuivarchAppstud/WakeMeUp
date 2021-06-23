package com.vguivarc.wakemeup.domain.entity

class AccountAlreadyExistsError : Throwable() {
    companion object {
        const val HTTP_ERROR_CODE = "ACCOUNT_ALREADY_CREATED"
    }
}
