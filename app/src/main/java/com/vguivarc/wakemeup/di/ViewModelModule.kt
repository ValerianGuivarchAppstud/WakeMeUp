package com.vguivarc.wakemeup.di

import com.vguivarc.wakemeup.transport.account.AccountViewModel
import com.vguivarc.wakemeup.transport.alarmlist.AlarmListViewModel
import com.vguivarc.wakemeup.transport.auth.login.AuthViewModel
import com.vguivarc.wakemeup.transport.contactlist.ContactListViewModel
import com.vguivarc.wakemeup.transport.contactlistfacebook.ContactFacebookListViewModel
import com.vguivarc.wakemeup.transport.favoritelist.FavoriteListViewModel
import com.vguivarc.wakemeup.transport.search.SearchSongListViewModel
import com.vguivarc.wakemeup.transport.settings.SettingsViewModel
import com.vguivarc.wakemeup.viewmodel.RingingAlarmViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AccountViewModel(get(), get(), get()) }
    viewModel { AlarmListViewModel(get()) }
    viewModel { AuthViewModel(get(), get(), get()) }
    viewModel { ContactListViewModel(get()) }
    viewModel { ContactFacebookListViewModel(get(), get()) }
    viewModel { FavoriteListViewModel(get()) }
    viewModel { RingingAlarmViewModel(get()) }
    viewModel { SearchSongListViewModel(get(), get(), get()) }
    viewModel { SettingsViewModel(get()) }

}
