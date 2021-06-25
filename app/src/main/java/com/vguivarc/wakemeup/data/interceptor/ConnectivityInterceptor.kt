package com.vguivarc.wakemeup.data.interceptor

import android.content.Context
import com.vguivarc.wakemeup.util.adapters.isNetworkAvailable
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.util.adapters.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

/**
 * Detect loss of connectivity
 */
internal class ConnectivityInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (context.isNetworkAvailable()) {
            return chain.proceed(chain.request())
        } else {
            Timber.e("NoConnectivityException")
            throw NoConnectivityException(context.getString(R.string.no_connectivity_exception_user_message))
        }
    }
}
