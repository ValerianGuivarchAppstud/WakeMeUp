package com.vguivarc.wakemeup.transport.contact.contactlistfacebook

import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.ContactInteractor
import com.vguivarc.wakemeup.domain.external.ProfileInteractor
import com.vguivarc.wakemeup.domain.external.entity.ContactFacebook
import com.vguivarc.wakemeup.transport.contact.contactlist.ContactListScreenSideEffect
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber


class ContactFacebookListViewModel(
    private val profileInteractor: ProfileInteractor,
    private val contactInteractor: ContactInteractor
    ) :
    ContainerHost<ContactFacebookListState, ContactFacebookListSideEffect>, ViewModel() {

    override val container =
        container<ContactFacebookListState, ContactFacebookListSideEffect>(
            ContactFacebookListState(),
            onCreate = ::onCreate
        )

    private fun onCreate(initialState: ContactFacebookListState) {
        getContactFacebookList()
    }

    fun getContactFacebookList() = intent {
        reduce {
            state.copy(isLoading = true)
        }

        try {
            val list = contactInteractor.getContactFacebookList(profileInteractor.getFacebookAuthToken())

            reduce {
                state.copy(contactFacebookList = list.toList(), isLoading = false)
            }

        } catch (exception: Exception) {
            Timber.e(exception)

            reduce {
                state.copy(contactFacebookList = emptyList(), isLoading = false)
            }

//            postSideEffect(ContactFacebookListSideEffect.Toast(R.string.general_error))
        }
    }

    fun actionAddFacebookContact(contact : ContactFacebook) = intent {
        try {
            contactInteractor.saveContactStatus(contact.idProfile, contact.contact)
            val list = contactInteractor.getContactFacebookList(profileInteractor.getFacebookAuthToken())

            reduce {
                state.copy(contactFacebookList = list.toList(), isLoading = false)
            }

        } catch (exception: Exception) {
            Timber.e(exception)
            postSideEffect(ContactFacebookListSideEffect.Toast(R.string.general_error))
        }
    }

    fun ok() = intent {
        postSideEffect(ContactFacebookListSideEffect.Ok)
    }
}
/*
class ContactFacebookViewModel(
    private val sessionService: ISessionProvider,
    private val contactService: IContactProvider
) : BaseViewModel() {

    private var _contactFacebookList = MutableLiveData<Resource<List<ContactFacebook>>>()
    val contactFacebookList: LiveData<Resource<List<ContactFacebook>>>
        get() = _contactFacebookList



    fun getContactFacebookList(socialAuthToken: String) {
        contactService.getContactFacebookList(socialAuthToken)
            .applySchedulers()
            .doOnSubscribe { _contactFacebookList.postValue(Loading()) }
            .subscribe(
                {
                    _contactFacebookList.postValue(Success(it))
                },
                {
                    _contactFacebookList.postValue(Fail(it))
                }
            )
            .addTo(disposables)
    }
}*/
