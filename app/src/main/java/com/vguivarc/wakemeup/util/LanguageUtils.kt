package com.vguivarc.wakemeup.util

import java.util.Locale

fun getDefaultLanguage(): String {
    val language = Locale.getDefault().language
    return if (language == Locale.FRENCH.language)
        language
    else
        Locale.ENGLISH.language
}
