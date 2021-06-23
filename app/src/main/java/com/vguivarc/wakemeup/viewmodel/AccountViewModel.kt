package com.vguivarc.wakemeup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vguivarc.wakemeup.base.*
import com.vguivarc.wakemeup.domain.entity.UserProfile
import com.vguivarc.wakemeup.domain.service.AuthService
import com.vguivarc.wakemeup.domain.service.SessionService
import com.vguivarc.wakemeup.util.applySchedulers
import io.reactivex.rxkotlin.addTo

class AccountViewModel(
    private val authService: AuthService,
    private val sessionService: SessionService
) : BaseViewModel() {

    fun isUserConnected(): Boolean = authService.isUserConnected()

    fun logout() {
        authService.logout()
    }

    fun getAndUpdateUserInfo(): LiveData<Resource<UserProfile>> {
        val result = MutableLiveData<Resource<UserProfile>>()
        result.postValue(Loading())

        authService.getAndUpdateUserInfo()
            .applySchedulers()
            .subscribe(
                {
                    result.postValue(Success(it))
                },
                {
                    result.postValue(Fail(it))
                }
            ).addTo(disposables)

        return result
    }

    fun editAccount(nickname: String, email: String): LiveData<Resource<UserProfile>> {
        val result = MutableLiveData<Resource<UserProfile>>()
        result.postValue(Loading())

        authService.editAccount(nickname, email)
            .applySchedulers()
            .subscribe(
                {
                    result.postValue(Success(it))
                },
                {
                    result.postValue(Fail(it))
                }
            ).addTo(disposables)

        return result
    }

    fun getUserInfo(): UserProfile? {
        return sessionService.getUserProfile()
    }
}
