package com.vguivarc.wakemeup.connect

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserModel(
    val id: String,
    val imageUrl: String,
    val username: String,
    val facebookId : String
) : Parcelable {


    constructor() : this("", "", "", "")

}