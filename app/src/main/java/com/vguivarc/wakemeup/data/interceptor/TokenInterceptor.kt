package com.vguivarc.wakemeup.data.interceptor

import android.content.Context
import com.vguivarc.wakemeup.BuildConfig
import com.vguivarc.wakemeup.data.network.AuthApi
import com.vguivarc.wakemeup.data.entity.AuthRefreshRequest
import com.vguivarc.wakemeup.data.entity.UserToken
import com.vguivarc.wakemeup.domain.internal.ISessionProvider
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

const val HEADER_AUTHORIZATION = "Authorization"
const val HEADER_AUTHORIZATION_PREFIX = "Bearer "

class TokenInterceptor(
    private val sessionService: ISessionProvider,
    private val apiBaseURL: String,
    val context: Context
) :
    Interceptor {
    private var authApi: AuthApi? = null

    @Suppress("MagicNumber")
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = sessionService.getUserToken()

        return when {
            token == null -> chain.proceed(originalRequest) // not connected, let it go
            isTokenExpired(token.accessTokenExpiration) -> refreshTokenAndRetry(
                chain,
                originalRequest
            )
            else -> {
                // add token and execute the request
                val updateRequest =
                    originalRequest.newBuilder()
                        .header(HEADER_AUTHORIZATION, HEADER_AUTHORIZATION_PREFIX + token.accessToken).build()
                val response = chain.proceed(updateRequest)

                // if an authentication error refresh the token and replay the request
                if (response.code == 401) {
                    refreshTokenAndRetry(chain, updateRequest)
                } else {
                    response
                }
            }
        }
    }

    @Suppress("MagicNumber")
    private fun isTokenExpired(tokenDate: Long): Boolean {
        return Date(tokenDate * 1000).before(Date())
    }

    private fun refreshTokenAndRetry(chain: Interceptor.Chain, originalRequest: Request): Response {

        val token = refreshToken()
        val updateRequestBuilder = originalRequest.newBuilder()

        if (token != null)
            updateRequestBuilder.header("Authorization", HEADER_AUTHORIZATION_PREFIX + token.accessToken)

        // if token null, refresh failed => we could return an error before doing any request,
        // but we don't know what is this request
        // maybe it's legit even if refresh failed
        return chain.proceed(updateRequestBuilder.build())
    }

    /* TooGenericExceptionCaught error from detekt must be ignored because
     blockingGet can throw RuntimeException and Error, and there is no multicatch on Kotlin
     language for now */
    @Suppress("TooGenericExceptionCaught")
    private fun refreshToken(): UserToken? {
        val currentToken = sessionService.getUserToken()

        if (currentToken == null || isTokenExpired(currentToken.refreshTokenExpiration))
            return null

        if (this.authApi == null)
            this.authApi = createAuthApi()

        val newToken: UserToken? = try {
            this.authApi?.refresh(
                AuthRefreshRequest(
                    currentToken.accessToken,
                    currentToken.refreshToken
                )
            )
                ?.blockingGet()
        } catch (e: Exception) {
            null
        }

        var userToken: UserToken? = null
        newToken?.let {
            userToken = UserToken(
                newToken.accessToken,
                newToken.refreshToken,
                newToken.accessTokenExpiration,
                newToken.refreshTokenExpiration
            )
            sessionService.setUserToken(userToken!!)
        }
        return userToken
    }

    @Suppress("MagicNumber")
    private fun createAuthApi(): AuthApi {
        val okHttpClientBuilder = OkHttpClient.Builder().dispatcher(
            Dispatcher(
                ThreadPoolExecutor(
                    0, Integer.MAX_VALUE,
                    60, TimeUnit.SECONDS, SynchronousQueue()
                )
            )

        )
            .addInterceptor {
                val currentToken = sessionService.getUserToken()
                it.proceed(
                    it.request().newBuilder().header(
                        "Authorization",
                        HEADER_AUTHORIZATION_PREFIX + currentToken?.accessToken
                    ).header("Content-Type", "application/json")
                        .build()
                )
            }
        if (BuildConfig.DEBUG) {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(logger)
        }

        val okHttpClient = okHttpClientBuilder.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(apiBaseURL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(AuthApi::class.java)
    }
}
