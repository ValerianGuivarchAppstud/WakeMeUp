package com.vguivarc.wakemeup.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
class LinkFirebaseAndFacebookIds(
    val idFirebase: String,
    val idFacebook: String
) : Parcelable {


    @Suppress("unused")
    constructor() : this("", "")

}