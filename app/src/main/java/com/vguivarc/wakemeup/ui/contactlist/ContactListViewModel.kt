package com.vguivarc.wakemeup.ui.contactlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vguivarc.wakemeup.base.*
import com.vguivarc.wakemeup.domain.entity.Contact
import com.vguivarc.wakemeup.domain.entity.Favorite
import com.vguivarc.wakemeup.domain.service.ContactService
import com.vguivarc.wakemeup.util.applySchedulers
import io.reactivex.rxkotlin.addTo

class ContactListViewModel(private val contactService: ContactService) : BaseViewModel() {

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
