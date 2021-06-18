package com.vguivarc.wakemeup.util

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Sends the network request on a separate thread because it can not be sent on the main thread
 * Sends the result on the main thread to update UI
 */
fun <T> Single<T>.applySchedulers(): Single<T> {
    return subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Maybe<T>.applySchedulers(): Maybe<T> {
    return subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
}

fun Completable.applySchedulers(): Completable {
    return subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
}
