package com.vguivarc.wakemeup.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.repo.Repository

class NotifListeViewModel(val repo: Repository) : ViewModel() {

    private val notifListeState = MediatorLiveData<Map<String,NotificationMusicMe>>()
   // private val listeVueListeState = MediatorLiveData<Boolean>()
    //TODO trié (encorE...) par date
    fun getNotifLiveData(): LiveData<Map<String,NotificationMusicMe>> = notifListeState
    //fun getNotifVueLiveData(): LiveData<Boolean> = listeVueListeState


    init {
        notifListeState.addSource(repo.getNotifications()) { newListe ->
            notifListeState.value = newListe
        }

      /*  listeVueListeState.addSource(repo.getNotifVue()) { bool ->
            listeVueListeState.value = bool
        }*/
    }

    fun notifAffichee() {
        repo.notifAffichee()
    }

    fun deleteNotif(notifKey: String) {
        repo.deleteNotif(notifKey)
    }

}