package com.vguivarc.wakemeup.transport.notificationlist

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

class NotifListViewModel(
) :
    ContainerHost<NotifListState, NotifListSideEffect>, ViewModel() {

    override val container =
        container<NotifListState, NotifListSideEffect>(
            NotifListState(),
            onCreate = ::onCreate
        )

    private fun onCreate(initialState: NotifListState) {
        getNotifList()
    }

    fun getNotifList() = intent {
        reduce {
            state.copy(isLoading = true)
        }

        try {
//            val list = notificationInteractor.getWaitingNotifList()

/*            reduce {
             //   state.copy(notificationList = list.toList(), isLoading = false)
            }*/

        } catch (exception: Exception) {
            Timber.e(exception)

            reduce {
                state.copy(notificationList = emptyList(), isLoading = false)
            }

//            postSideEffect(NotifListSideEffect.Toast(R.string.general_error))
        }
    }

    fun ok() = intent {
        postSideEffect(NotifListSideEffect.Ok)
    }
}