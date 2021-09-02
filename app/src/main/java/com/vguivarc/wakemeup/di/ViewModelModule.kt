package com.vguivarc.wakemeup.di

import com.vguivarc.wakemeup.ui.contactlist.ContactListViewModel
import com.vguivarc.wakemeup.ui.contactlistfacebook.ContactFacebookViewModel
import com.vguivarc.wakemeup.ui.favoritelist.FavoriteListViewModel
import com.vguivarc.wakemeup.ui.search.SearchSongViewModel
import com.vguivarc.wakemeup.viewmodel.AccountViewModel
import com.vguivarc.wakemeup.viewmodel.AlarmsViewModel
import com.vguivarc.wakemeup.viewmodel.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AccountViewModel(get(), get()) }
    viewModel { AlarmsViewModel(get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { ContactListViewModel(get()) }
    viewModel { ContactFacebookViewModel(get(), get()) }
    viewModel { FavoriteListViewModel(get()) }
    viewModel { SearchSongViewModel(get(), get(), get()) }

}
