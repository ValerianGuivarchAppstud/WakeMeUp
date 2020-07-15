package com.vguivarc.wakemeup.sonnerie


data class MusiqueListViewState(
    val notifActivated : Boolean,
    val musiques : Map<String, Sonnerie>
)