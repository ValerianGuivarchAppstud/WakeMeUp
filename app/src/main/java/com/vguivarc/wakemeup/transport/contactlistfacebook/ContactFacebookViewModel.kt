package com.vguivarc.wakemeup.transport.contactlistfacebook

import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.domain.external.ContactInteractor
import com.vguivarc.wakemeup.domain.external.SessionInteractor
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber


class ContactFacebookListViewModel(
    private val sessionInteractor: SessionInteractor,
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
            val list = contactInteractor.getContactFacebookList(sessionInteractor.getFacebookAuthToken())

            reduce {
                state.copy(contactFacebookList = list, isLoading = false)
            }

        } catch (exception: Exception) {
            Timber.e(exception)

            reduce {
                state.copy(contactFacebookList = emptyList(), isLoading = false)
            }

//            postSideEffect(ContactFacebookListSideEffect.Toast(R.string.general_error))
        }
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
