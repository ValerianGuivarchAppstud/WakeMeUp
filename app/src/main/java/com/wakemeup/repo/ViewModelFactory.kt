package com.neocampus.repo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wakemeup.contact.ContactListeViewModel
import com.wakemeup.reveil.ReveilListeViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ReveilListeViewModel::class.java) -> ReveilListeViewModel(
                repository
            )
            modelClass.isAssignableFrom(ContactListeViewModel::class.java) -> ContactListeViewModel(
                repository
            )
            else -> throw IllegalArgumentException("Unexpected model class $modelClass")
        } as T
    }
}