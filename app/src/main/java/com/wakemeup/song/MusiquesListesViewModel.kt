package com.wakemeup.song

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.neocampus.repo.Repository
import com.wakemeup.AppWakeUp
import com.wakemeup.contact.SonnerieRecue

class MusiquesListesViewModel() : ViewModel() {

    private val sonneriesPasseesListeState = MediatorLiveData<Map<String, SonnerieRecue>>()
    private val sonneriesAttenteListeState = MediatorLiveData<Map<String, SonnerieRecue>>()

    init {
        sonneriesPasseesListeState.addSource(AppWakeUp.getSonneriesPassees()) { newListe ->
            sonneriesPasseesListeState.value = newListe
        }
        sonneriesAttenteListeState.addSource(AppWakeUp.getSonneriesAttente()) { newListe ->
            sonneriesAttenteListeState.value = newListe
        }
    }

    fun getListePasseesLiveData(): LiveData<Map<String, SonnerieRecue>> = sonneriesPasseesListeState
    fun getListeAttenteLiveData(): LiveData<Map<String, SonnerieRecue>> = sonneriesAttenteListeState

    fun addSonnerieAttente(idSonnerie: String, sonnerie: SonnerieRecue, context: Context) {
        AppWakeUp.addSonnerieEnAttente(idSonnerie, sonnerie, context)
    }

    fun removeSonnerieEnAttente(idSonnerie: String, musicId: String, context: Context) {
        AppWakeUp.removeSonnerieEnAttente(idSonnerie, musicId, context)
    }

}