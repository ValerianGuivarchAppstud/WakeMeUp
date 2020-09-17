package com.vguivarc.wakemeup.connect

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
class LinkFirebaseAndFacebookIds(
    val idFirebase: String,
    val idFacebook: String
) : Parcelable {


    constructor() : this("", "")

}