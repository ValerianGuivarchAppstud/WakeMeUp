package com.wakemeup.connect

class UserModel(
    val id: String,
    val imageUrl: String,
    val phone: String,
    val username: String,
    val mail: String
) {


    constructor() : this("", "", "", "", "")

}