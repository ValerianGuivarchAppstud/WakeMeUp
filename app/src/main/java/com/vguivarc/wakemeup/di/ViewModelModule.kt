package com.vguivarc.wakemeup.di

import com.vguivarc.wakemeup.transport.MainActivityViewModel
import com.vguivarc.wakemeup.transport.account.profile.ProfileViewModel
import com.vguivarc.wakemeup.transport.alarm.AlarmListViewModel
import com.vguivarc.wakemeup.transport.account.login.AuthViewModel
import com.vguivarc.wakemeup.transport.contact.contactlist.ContactListViewModel
import com.vguivarc.wakemeup.transport.contact.contactlistfacebook.ContactFacebookListViewModel
import com.vguivarc.wakemeup.transport.favoritelist.FavoriteListViewModel
import com.vguivarc.wakemeup.transport.ringingalarm.RingingAlarmViewModel
import com.vguivarc.wakemeup.transport.search.SearchSongListViewModel
import com.vguivarc.wakemeup.transport.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { AlarmListViewModel(get()) }
    viewModel { AuthViewModel(get(), get()) }
    viewModel { ContactListViewModel(get()) }
    viewModel { ContactFacebookListViewModel(get(), get()) }
    viewModel { FavoriteListViewModel(get()) }
    viewModel { MainActivityViewModel(get(), get()) }
    viewModel { RingingAlarmViewModel(get(), get()) }
    viewModel { SearchSongListViewModel(get(), get()) }
    viewModel { SettingsViewModel(get()) }

}
