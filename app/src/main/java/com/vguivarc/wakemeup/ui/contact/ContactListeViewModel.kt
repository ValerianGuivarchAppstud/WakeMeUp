package com.vguivarc.wakemeup.ui.contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.data.repository.Repository

class ContactListeViewModel(val repo: Repository) : ViewModel() {

    private val contactListeState = MediatorLiveData<FacebookResult>()
    fun getContactsListeLiveData(): LiveData<FacebookResult> = contactListeState

    init {
        contactListeState.addSource(repo.getFacebookListLiveData()) { newContacts ->
            contactListeState.value = newContacts
        }
    }

    fun getContacts() {
        repo.requestFacebookFriendData()
    }
}
