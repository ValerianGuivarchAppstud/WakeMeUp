package com.vguivarc.wakemeup.contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.connect.UserModel
import com.vguivarc.wakemeup.repo.Repository
import com.vguivarc.wakemeup.song.favori.VideoFavoriResult
import com.vguivarc.wakemeup.util.AddFireBaseObjectResult

class ContactListeViewModel(val repo: Repository) : ViewModel(){

    private val addContactState = MediatorLiveData<AddFireBaseObjectResult>()
    fun getAddContactStateLiveData(): LiveData<AddFireBaseObjectResult> = addContactState

    private val contactListeState = MediatorLiveData<ContactResult>()
    fun getContactsListeLiveData(): LiveData<ContactResult> = contactListeState

    init {
        contactListeState.addSource(repo.getContactListLiveData()) { newContacts ->
            contactListeState.value = newContacts
        }
        addContactState.addSource(repo.getContactStateAddResult()) { newContactResults ->
            addContactState.value = newContactResults
        }

    }

    fun removeContact(newUser: UserModel) {
        repo.deleteContact(newUser)
    }

    fun addContact(newUser: UserModel) {
        repo.addContact(newUser)
    }

    fun getContacts() {
        repo.getContact()
    }
}