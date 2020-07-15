package com.vguivarc.wakemeup.notification

import androidx.lifecycle.*
import com.vguivarc.wakemeup.connect.UserModel
import com.vguivarc.wakemeup.repo.Repository
import com.vguivarc.wakemeup.song.Song
import com.vguivarc.wakemeup.util.AddFireBaseObjectResult

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