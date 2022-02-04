package com.vguivarc.wakemeup.transport.contact.addbyusername

import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.ContactInteractor
import com.vguivarc.wakemeup.domain.external.ProfileInteractor
import com.vguivarc.wakemeup.domain.external.entity.ContactFacebook
import com.vguivarc.wakemeup.domain.external.entity.SearchSong
import com.vguivarc.wakemeup.domain.external.entity.UserProfile
import com.vguivarc.wakemeup.transport.contact.contactlist.ContactListScreenSideEffect
import com.vguivarc.wakemeup.transport.contact.contactlistfacebook.ContactFacebookListSideEffect
import com.vguivarc.wakemeup.transport.search.SearchSongListSideEffect
import com.vguivarc.wakemeup.util.toSong
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber


class AddByUsernameViewModel(
    private val contactInteractor: ContactInteractor
    ) :
    ContainerHost<AddByUsernameState, AddByUsernameSideEffect>, ViewModel() {

    override val container =
        container<AddByUsernameState, AddByUsernameSideEffect>(
            AddByUsernameState(),
            onCreate = ::onCreate
        )

    private fun onCreate(initialState: AddByUsernameState) {
    }

    fun setSearchUsernameText(searchText: String)  = intent {
        reduce {
            state.copy(isSearchLoading = false, showBeforeSearch = false, showEmptyResult = false, user = null, searchText = searchText)
        }
    }



    fun getSearchedUser() = intent {
        reduce {
            state.copy(isSearchLoading = true, showBeforeSearch = false, showEmptyResult = false, user = null)
        }

        try {
            val result = contactInteractor.searchByUsername(state.searchText)

            val searchResult = if(result.foundResult) {
                Pair(UserProfile(result.idProfile, null, result.username, null, result.pictureUrl), result.contact)
            } else {
                null
            }
            reduce {
                if(searchResult == null) {
                    state.copy(isSearchLoading = false, showBeforeSearch = false, showEmptyResult = true, user = null)
                } else {
                    state.copy(isSearchLoading = false, showBeforeSearch = false, showEmptyResult = false, user = searchResult.first, isContact = searchResult.second)
                }
            }

        } catch (exception: Exception) {
            Timber.e(exception)

            reduce {
                state.copy(isSearchLoading = false, showBeforeSearch = false, showEmptyResult = true, user = null)
            }

            postSideEffect(AddByUsernameSideEffect.Toast(R.string.general_error))
        }
    }



    fun changeStatusContact() = intent {
        reduce {
            state.copy(isContactLoading = true)
        }
        try {

           state.user?.let{
                contactInteractor.saveContactStatus(it.profileId, state.isContact.not())
            }

            reduce {
                state.copy(isContactLoading = false, isContact = state.isContact.not())
            }

        } catch (exception: Exception) {
            Timber.e(exception)
            postSideEffect(AddByUsernameSideEffect.Toast(R.string.general_error))
        }
    }

    fun ok() = intent {
        postSideEffect(AddByUsernameSideEffect.Ok)
    }
}