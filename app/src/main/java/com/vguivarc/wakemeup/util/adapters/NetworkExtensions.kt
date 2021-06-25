package com.vguivarc.wakemeup.util.adapters

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

/**
 * Check for a wifi or mobile connection, but does not check if Internet is reachable
 * @return true if a wifi or mobile connection is available
 */
@Suppress("ComplexMethod")
fun Context.isNetworkAvailable(): Boolean {
    var result = false
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities)
        if (actNw != null) {
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        }
    } else {
        @Suppress("DEPRECATION")
        connectivityManager.activeNetworkInfo?.run {
            result = when (type) {
                ConnectivityManager.TYPE_WIFI -> true
                ConnectivityManager.TYPE_MOBILE -> true
                ConnectivityManager.TYPE_ETHERNET -> true
                ConnectivityManager.TYPE_BLUETOOTH -> true
                ConnectivityManager.TYPE_VPN -> true
                ConnectivityManager.TYPE_WIMAX -> true
                ConnectivityManager.TYPE_MOBILE_DUN -> true
                ConnectivityManager.TYPE_MOBILE_HIPRI -> true
                ConnectivityManager.TYPE_MOBILE_MMS -> true
                ConnectivityManager.TYPE_MOBILE_SUPL -> true
                else -> false
            }
        }
    }
    return result
}
