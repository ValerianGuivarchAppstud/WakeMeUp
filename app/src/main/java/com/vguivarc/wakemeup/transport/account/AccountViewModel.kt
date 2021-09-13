package com.vguivarc.wakemeup.transport.account

import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.AuthInteractor
import com.vguivarc.wakemeup.domain.external.ProfileInteractor
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import com.vguivarc.wakemeup.util.isValidEmail
import com.vguivarc.wakemeup.util.isValidUsername
import org.orbitmvi.orbit.syntax.simple.postSideEffect


class AccountViewModel(private val authInteractor: AuthInteractor,
                       private val profileInteractor: ProfileInteractor
) :
    ContainerHost<AccountState, AccountSideEffect>, ViewModel() {

    override val container =
        container<AccountState, AccountSideEffect>(
            AccountState(),
            onCreate = ::onCreate
        )

    private fun onCreate(initialState: AccountState) {
        getUserProfileSession()
    }

    fun getUserProfileSession() = intent {
        reduce {
            state.copy(isLoading = true)
        }

        try {
            val userProfile = profileInteractor.getAndUpdateUserInfo()

            reduce {
                state.copy(userProfile = userProfile, isLoading = false, isConnected = true)
            }

        } catch (exception: Exception) {
            Timber.e(exception)

            reduce {
                state.copy(userProfile = null, isLoading = false)
            }

            postSideEffect(AccountSideEffect.Toast(R.string.general_error))
        }

    }


    fun logout() = intent {
        try {
            authInteractor.logout()

            reduce {
                state.copy(userProfile = null, isLoading = false, isConnected = false)
            }

        } catch (exception: Exception) {
            Timber.e(exception)

            reduce {
                state.copy(userProfile = null, isLoading = false)
            }

            // postSideEffect(AlarmListSideEffect.Toast(R.string.general_error))
        }
    }

    fun submitAccountEdit(email: String, username: String) = intent {
        reduce {
            state.copy(isLoading = true)
        }

        try {
            if (!email.isValidEmail())
                postSideEffect(AccountSideEffect.Toast(R.string.form_email_format_error))

            if (!username.isValidUsername())
                postSideEffect(AccountSideEffect.Toast(R.string.form_username_format_error))
            if ( email.isValidEmail() && username.isValidUsername())
            profileInteractor.editAccount(
                username = username,
                email = email
            )
        } catch (exception: Exception) {
            Timber.e(exception)

            reduce {
                state.copy(userProfile = null, isLoading = false)
            }

            // postSideEffect(AlarmListSideEffect.Toast(R.string.general_error))
        }
    }
    /*

    suspend fun getAndUpdateUserInfo(): LiveData<Resource<UserProfile>> {
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

    fun editAccount(username: String, email: String): LiveData<Resource<UserProfile>> {
        val result = MutableLiveData<Resource<UserProfile>>()
        result.postValue(Loading())

        authService.editAccount(username, email)
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
        return sessionService.getUserProfileSession()
    }*/
}
