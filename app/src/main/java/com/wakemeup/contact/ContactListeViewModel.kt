package com.wakemeup.contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.neocampus.repo.Repository
import com.wakemeup.connect.UserModel

class ContactListeViewModel(val repo: Repository) : ViewModel(){

    private val contactListeState = MediatorLiveData<Map<String, UserModel>>()

    init {
        contactListeState.addSource(repo.getContacts()) { newContacts ->
            contactListeState.value = newContacts
        }
    }

    fun getContactsListeLiveData(): LiveData<Map<String, UserModel>> = contactListeState

    fun addContact(newUser: UserModel) {
        repo.addContact(newUser)
    }
}