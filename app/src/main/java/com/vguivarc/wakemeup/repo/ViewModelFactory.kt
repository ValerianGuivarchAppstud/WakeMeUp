package com.vguivarc.wakemeup.repo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vguivarc.wakemeup.CurrentUserViewModel
import com.vguivarc.wakemeup.connect.ui.login.FbLoginViewModel
import com.vguivarc.wakemeup.connect.ui.login.LoginViewModel
import com.vguivarc.wakemeup.connect.ui.signup.SignupViewModel
import com.vguivarc.wakemeup.contact.ContactListeViewModel
import com.vguivarc.wakemeup.facebook.FacebookListeViewModel
import com.vguivarc.wakemeup.notification.NotifListeViewModel
import com.vguivarc.wakemeup.reveil.ReveilListeViewModel
import com.vguivarc.wakemeup.song.favori.FavorisViewModel
import com.vguivarc.wakemeup.song.search.RechercheVideoViewModel
import com.vguivarc.wakemeup.sonnerie.SonnerieListeViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ReveilListeViewModel::class.java) -> ReveilListeViewModel(
                repository
            )
            modelClass.isAssignableFrom(RechercheVideoViewModel::class.java) -> RechercheVideoViewModel(repository
            )
            modelClass.isAssignableFrom(ContactListeViewModel::class.java) -> ContactListeViewModel(
                repository
            )
            modelClass.isAssignableFrom(FacebookListeViewModel::class.java) -> FacebookListeViewModel(
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
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> SignupViewModel(
                repository
            )
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(
                repository
            )
            modelClass.isAssignableFrom(FbLoginViewModel::class.java) -> FbLoginViewModel(
                repository
            )
            modelClass.isAssignableFrom(NotifListeViewModel::class.java) -> NotifListeViewModel(
                repository
            )
            else -> throw IllegalArgumentException("Unexpected model class $modelClass")
        } as T
    }
}