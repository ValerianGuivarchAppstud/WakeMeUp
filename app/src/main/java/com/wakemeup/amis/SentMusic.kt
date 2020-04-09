package com.wakemeup.amis

import java.io.Serializable


class SentMusic(val sender: String, val receiver: String, val song: String) : Serializable {

    constructor() : this("", "", "")
}
