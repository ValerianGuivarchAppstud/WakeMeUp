package com.vguivarc.wakemeup.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.vguivarc.wakemeup.BuildConfig
import com.vguivarc.wakemeup.data.interceptor.ConnectivityInterceptor
import com.vguivarc.wakemeup.data.interceptor.TokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit


val facebookNetworkModule = module {

    // single(named(DI_MOCK_ENABLED)) { BuildConfig.MOCK_ENABLED }

    // Initialise the network
    single<Retrofit> (named("RetrofitFacebook")){
        Retrofit.Builder()
            .baseUrl("http.graph.facebook.com")
            .client(get(named("OkHttpClientFacebook")))
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    // Initialize OK HTTP Client
    single<OkHttpClient>((named("OkHttpClientFacebook"))) {
        OkHttpClient.Builder()
            .apply {
                readTimeout(READ_TIMEOUT_IN_MINUTES, TimeUnit.MINUTES)

                addInterceptor(ConnectivityInterceptor(get()))

                // Access token
                addInterceptor(TokenInterceptor(get(), BuildConfig.API_BASEURL, get()))

/*                if (get(named(DI_MOCK_ENABLED))) {
                     addInterceptor((get<MockInterceptor>()))
                }*/

                // Logger
                if (BuildConfig.DEBUG) {
                    val logger = HttpLoggingInterceptor()
                    logger.level = HttpLoggingInterceptor.Level.BODY
                    addInterceptor(logger)
                }
            }.build()
    }
}
