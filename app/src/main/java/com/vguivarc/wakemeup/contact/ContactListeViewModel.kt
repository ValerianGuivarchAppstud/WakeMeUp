package com.vguivarc.wakemeup.contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.facebook.FacebookResult
import com.vguivarc.wakemeup.repo.Repository

class ContactListeViewModel(val repo: Repository) : ViewModel(){

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