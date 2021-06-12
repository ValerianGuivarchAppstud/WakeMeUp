package com.vguivarc.wakemeup.repo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vguivarc.wakemeup.CurrentUserViewModel
import com.vguivarc.wakemeup.data.repository.Repository
import com.vguivarc.wakemeup.ui.connect.viewmodel.FbLoginViewModel
import com.vguivarc.wakemeup.ui.contact.ContactListeViewModel
import com.vguivarc.wakemeup.notification.NotifListeViewModel
import com.vguivarc.wakemeup.ui.alarm.AlarmsViewModel
import com.vguivarc.wakemeup.ui.favori.FavorisViewModel
import com.vguivarc.wakemeup.ui.search.RechercheVideoViewModel
import com.vguivarc.wakemeup.ui.sonnerie.SonnerieListeViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(RechercheVideoViewModel::class.java) -> RechercheVideoViewModel(repository
            )
            modelClass.isAssignableFrom(ContactListeViewModel::class.java) -> ContactListeViewModel(
                repository
            )
                    modelClass.isAssignableFrom(FavorisViewModel::class.java) -> FavorisViewModel(
                repository
            )
            modelClass.isAssignableFrom(SonnerieListeViewModel::class.java) -> SonnerieListeViewModel(
                repository
            )
            modelClass.isAssignableFrom(CurrentUserViewModel::class.java) -> CurrentUserViewModel(
                repository
            )
            modelClass.isAssignableFrom(NotifListeViewModel::class.java) -> NotifListeViewModel(
                repository
            )
            modelClass.isAssignableFrom(FbLoginViewModel::class.java) -> FbLoginViewModel(
                repository
            )
            else -> throw IllegalArgumentException("Unexpected model class $modelClass")
        } as T
    }
}