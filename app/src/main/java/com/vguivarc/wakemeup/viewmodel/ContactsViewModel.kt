package com.vguivarc.wakemeup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vguivarc.wakemeup.base.*
import com.vguivarc.wakemeup.data.entity.ShareRingingRequest
import com.vguivarc.wakemeup.domain.entity.Contact
import com.vguivarc.wakemeup.domain.entity.Ringing
import com.vguivarc.wakemeup.domain.service.ContactService
import com.vguivarc.wakemeup.util.SingleLiveEvent
import com.vguivarc.wakemeup.util.applySchedulers
import io.reactivex.rxkotlin.addTo

class ContactsViewModel(
    private val contactsService: ContactService
) : BaseViewModel() {

    private val _contactsList = MutableLiveData<Resource<List<Contact>>>()
    val contactsList: LiveData<Resource<List<Contact>>>
        get() = _contactsList

    private val _ringingHasBeenShared = SingleLiveEvent<Resource<Ringing>>()
    val ringingHasBeenShared: LiveData<Resource<Ringing>>
        get() = _ringingHasBeenShared

    fun getContacts() {
        contactsService.getContact()
            .applySchedulers()
            .doOnSubscribe { _contactsList.postValue(Loading()) }
            .subscribe(
                {
                    _contactsList.postValue(Success(it))
                },
                {
                    _contactsList.postValue(Fail(it))
                }
            )
            .addTo(disposables)
    }

    fun shareRinging(shareRingingRequest: ShareRingingRequest) {
        contactsService.shareRinging(shareRingingRequest)
            .applySchedulers()
            .doOnSubscribe {
                _ringingHasBeenShared.postValue(Loading())
            }
            .subscribe(
                { ringing ->
                    _ringingHasBeenShared.postValue(Success(ringing))
                },
                {
                    _ringingHasBeenShared.postValue(Fail(it))
                }
            )
            .addTo(disposables)
    }
}
