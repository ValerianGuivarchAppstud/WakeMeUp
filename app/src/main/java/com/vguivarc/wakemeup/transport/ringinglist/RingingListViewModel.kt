package com.vguivarc.wakemeup.transport.ringinglist

import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.domain.external.RingingInteractor
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

class RingingListViewModel(
    private val ringingInteractor: RingingInteractor
) :
    ContainerHost<RingingListState, RingingListSideEffect>, ViewModel() {

    override val container =
        container<RingingListState, RingingListSideEffect>(
            RingingListState(),
            onCreate = ::onCreate
        )

    private fun onCreate(initialState: RingingListState) {
        getRingingList()
    }

    fun getRingingList() = intent {
        reduce {
            state.copy(isLoading = true)
        }

        try {
            val list = ringingInteractor.getWaitingRingingList()

            reduce {
                state.copy(ringingList = list.toList(), isLoading = false)
            }

        } catch (exception: Exception) {
            Timber.e(exception)

            reduce {
                state.copy(ringingList = emptyList(), isLoading = false)
            }

//            postSideEffect(RingingListSideEffect.Toast(R.string.general_error))
        }
    }

    fun ok() = intent {
        postSideEffect(RingingListSideEffect.Ok)
    }
}