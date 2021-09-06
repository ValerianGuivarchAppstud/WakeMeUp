package com.vguivarc.wakemeup.transport.contactlist

import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.domain.external.ContactInteractor
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

/*
class ContactListViewModel(private val contactService: IContactProvider) : BaseViewModel() {

    private val _contactList = MutableLiveData<Resource<List<Contact>>>()
    val contactList: LiveData<Resource<List<Contact>>>
        get() = _contactList

    fun getContactList() {
        contactService.getContactList()
            .applySchedulers()
            .doOnSubscribe { _contactList.postValue(Loading()) }
            .subscribe(
                {
                    _contactList.postValue(Success(it))
                },
                {
                    _contactList.postValue(Fail(it))
                }
            )
            .addTo(disposables)
    }
}
*/

class ContactListViewModel(private val contactInteractor: ContactInteractor) :
    ContainerHost<ContactListState, ContactListSideEffect>, ViewModel() {

    override val container =
        container<ContactListState, ContactListSideEffect>(
            ContactListState(),
            onCreate = ::onCreate
        )

    private fun onCreate(initialState: ContactListState) {
        getContactList()
    }

    fun getContactList() = intent {
        reduce {
            state.copy(isLoading = true)
        }

        try {
            val list = contactInteractor.getContactList()

            reduce {
                state.copy(contactList = list, isLoading = false, isFabOpen = false)
            }

        } catch (exception: Exception) {
            Timber.e(exception)

            reduce {
                state.copy(contactList = emptyList(), isLoading = false, isFabOpen = false)
            }

//            postSideEffect(ContactListSideEffect.Toast(R.string.general_error))
        }
    }

    fun addContactButton()  = intent {
        reduce {
            Timber.e("change")
            state.copy(isFabOpen = !(state.isFabOpen))
        }
    }
}
