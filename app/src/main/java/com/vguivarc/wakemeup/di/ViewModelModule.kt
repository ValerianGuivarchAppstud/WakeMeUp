package com.vguivarc.wakemeup.di

import com.vguivarc.wakemeup.viewmodel.AlarmsViewModel
import com.vguivarc.wakemeup.viewmodel.AuthViewModel
import com.vguivarc.wakemeup.ui.music.FavoriteViewModel
import com.vguivarc.wakemeup.ui.search.SongViewModel
import com.vguivarc.wakemeup.viewmodel.AccountViewModel
import com.vguivarc.wakemeup.viewmodel.ContactsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AccountViewModel(get(), get()) }
    viewModel { AlarmsViewModel(get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { ContactsViewModel(get()) }
    viewModel { FavoriteViewModel(get()) }
    viewModel { SongViewModel(get()) }
}
