package com.vguivarc.wakemeup.di

import com.vguivarc.wakemeup.ui.alarm.AlarmsViewModel
import com.vguivarc.wakemeup.ui.music.FavoriteViewModel
import com.vguivarc.wakemeup.ui.search.SearchSongViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
 viewModel { AlarmsViewModel(get()) }
 viewModel { FavoriteViewModel(get()) }
 viewModel { SearchSongViewModel(get()) }
/* viewModel { AuthViewModel(get()) }
 viewModel { HomeViewModel(get()) }
 viewModel { AccountViewModel(get(), get(), get(), get()) }
 viewModel { CatalogViewModel(get()) }
 viewModel { MagazinesViewModel(get()) }
 viewModel { SerieViewModel(get(), get(), get(), get()) }
 viewModel { ReaderSerieViewModel(get(), get(), get()) }
 viewModel { ReaderMagazineViewModel(get()) }
 viewModel { CommentViewModel(get()) }
 viewModel { DownloadViewModel(get()) }
 viewModel { OnboardingViewModel(get(), get()) }
 viewModel { CoinViewModel(get(), get(), get()) }*/
}
