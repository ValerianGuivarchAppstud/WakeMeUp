/*
 * MIT License
 *
 * Copyright (c) 2017 Appstud
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.vguivarc.wakemeup.di

import com.vguivarc.wakemeup.data.repository.AlarmRepository
import com.vguivarc.wakemeup.data.repository.FavoriteRepository
import com.vguivarc.wakemeup.data.repository.SongRepository
import com.vguivarc.wakemeup.domain.service.AlarmService
import com.vguivarc.wakemeup.domain.service.FavoriteService
import com.vguivarc.wakemeup.domain.service.SongService
import org.koin.dsl.module

val servicesModule = module {
    single<AlarmService> { AlarmRepository() }
    single<FavoriteService> { FavoriteRepository() }
    single<SongService> { SongRepository() }

    /*single<EpisodeService> { EpisodeRepository(get(), get()) }
    single<CatalogService> { CatalogRepository(get(), get()) }
    single<MagazinesService> { MagazinesRepository(get(), get()) }
    single<SerieService> { SerieRepository(get(), get()) }
    single<HomeService> { HomeRepository(get(), get()) }
    single<SessionService> { SessionRepository(get(), get()) }
    single<AuthService> { AuthRepository(get(), get()) }
    single<CommentService> { CommentRepository(get()) }
    single<FirebaseService> { FirebaseRepository() }
    single<DownloadService> { DownloadRepository(get(), get(), get()) }
    single<OnboardingService> { OnboardingRepository(get()) }
    single<ShopService> { ShopRepository(get(), get()) }
    single<IAnalyticsProvider> { MixpanelAnalyticsProvider() }*/
}
