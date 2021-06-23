package com.vguivarc.wakemeup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vguivarc.wakemeup.base.*
import com.vguivarc.wakemeup.domain.entity.AccountAlreadyExistsError
import com.vguivarc.wakemeup.domain.entity.FacebookMissingMailError
import com.vguivarc.wakemeup.domain.service.AuthService
import com.vguivarc.wakemeup.util.applySchedulers
import io.reactivex.rxkotlin.addTo
import retrofit2.HttpException

/**
 * This view model handles auth logic
 *
 * @param authService instance of AuthService for auth related api requests (login, signup)
 */
class AuthViewModel(
    private val authService: AuthService
) : BaseViewModel() {

    fun login(email: String, password: String): LiveData<Resource<Unit>> {

        val result = MutableLiveData<Resource<Unit>>()
        result.postValue(Loading())

        authService.login(email, password)
            .applySchedulers()
            .subscribe(
                {
                    result.postValue(Success(Unit))
                },
                {
                    result.postValue(Fail(it))
                }
            ).addTo(disposables)

        return result
    }

    fun loginWithFacebook(token: String): LiveData<Resource<Unit>> {

        val result = MutableLiveData<Resource<Unit>>()
        result.postValue(Loading())

        authService.loginWithFacebook(token)
            .applySchedulers()
            .subscribe(
                {
                    result.postValue(Success(Unit))
                },
                {
                    if (isFacebookError(it))
                        result.postValue(Fail(FacebookMissingMailError()))
                    else
                        result.postValue(Fail(it))
                }
            ).addTo(disposables)

        return result
    }

    fun signup(email: String, password: String, nickname: String): LiveData<Resource<Unit>> {
        val result = MutableLiveData<Resource<Unit>>()
        result.postValue(Loading())

        authService.signup(email, password, nickname)
            .applySchedulers()
            .subscribe(
                {
                    result.postValue(Success(Unit))
                },
                {
                    if (isAccountAlreadyExistsError(it))
                        result.postValue(Fail(AccountAlreadyExistsError()))
                    else
                        result.postValue(Fail(it))
                }
            ).addTo(disposables)

        return result
    }

    fun forgotPassword(email: String): LiveData<Resource<Unit>> {
        val result = MutableLiveData<Resource<Unit>>()
        result.postValue(Loading())

        authService.forgotPassword(email)
            .applySchedulers()
            .subscribe(
                {
                    result.postValue(Success(Unit))
                },
                {
                    result.postValue(Fail(it))
                }
            ).addTo(disposables)

        return result
    }

    private fun isFacebookError(throwable: Throwable?): Boolean =
        isThisError(FacebookMissingMailError.HTTP_ERROR_CODE, throwable)

    private fun isAccountAlreadyExistsError(throwable: Throwable?): Boolean =
        isThisError(AccountAlreadyExistsError.HTTP_ERROR_CODE, throwable)

    private fun isThisError(errorCode: String, throwable: Throwable?): Boolean =
        throwable is HttpException &&
            throwable.response()?.errorBody()?.string()
            ?.contains(errorCode) == true
}
