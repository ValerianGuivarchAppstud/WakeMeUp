package com.vguivarc.wakemeup.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.vguivarc.wakemeup.BuildConfig
import com.vguivarc.wakemeup.data.interceptor.ConnectivityInterceptor
import com.vguivarc.wakemeup.data.interceptor.TokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

const val READ_TIMEOUT_IN_MINUTES: Long = 3L
const val DI_MOCK_ENABLED = "mockEnabled"

val networkModule = module {

    // Initialise the network
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASEURL)
            .client(get<OkHttpClient>())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    // Initialize OK HTTP Client
    single<OkHttpClient> {
        OkHttpClient.Builder()
            .apply {
                readTimeout(READ_TIMEOUT_IN_MINUTES, TimeUnit.MINUTES)

                addInterceptor(ConnectivityInterceptor(get()))

                // Access token
                 addInterceptor(TokenInterceptor(get(), BuildConfig.API_BASEURL, get()))

                // Logger
                if (BuildConfig.DEBUG) {
                    val logger = HttpLoggingInterceptor()
                    logger.level = HttpLoggingInterceptor.Level.BODY
                    addInterceptor(logger)
                }
            }.build()
    }

    // Initialize Moshi
    single<Moshi> {
        Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .build()
    }
}
