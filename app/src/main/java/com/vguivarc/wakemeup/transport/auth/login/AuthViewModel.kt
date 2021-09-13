package com.vguivarc.wakemeup.transport.auth.login

import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.base.*
import com.vguivarc.wakemeup.domain.external.AuthInteractor
import com.vguivarc.wakemeup.domain.external.ProfileInteractor
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

/**
 * This view model handles auth logic
 *
 * @param authService instance of AuthService for auth related api requests (login, signup)
 */


class AuthViewModel(private val authInteractor: AuthInteractor,
                    private val profileInteractor: ProfileInteractor
) :
    ContainerHost<AuthState, AuthSideEffect>, ViewModel() {


    override val container =
        container<AuthState, AuthSideEffect>(
            AuthState(),
            onCreate = ::onCreate
        )

    private fun onCreate(initialState: AuthState) {

    }

    fun loginWithFacebook(token: String)  = intent {
        reduce {
            state.copy(isLoading = true)
        }
        try {
            authInteractor.loginWithFacebook(token)

            reduce {
                state.copy(isConnected = true, isLoading = false)
            }

        } catch (exception: Exception) {
            Timber.e(exception)

            reduce {
                state.copy(isConnected = false, isLoading = false)
            }

            postSideEffect(AuthSideEffect.Toast(R.string.general_error))
        }
    }
}
/*
class AuthViewModel(
    private val authService: IAuthProvider
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
*/