package com.wakemeup.song

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.neocampus.repo.Repository
import com.wakemeup.AppWakeUp
import com.wakemeup.contact.SonnerieRecue

data class MusiquesListViewState(
    val hasMusiquesChanged : Boolean,
    val musiques : Map<String, SonnerieRecue>
)

class MusiquesListesViewModel() : ViewModel() {

    private val sonneriesPasseesListeState = MediatorLiveData<MusiquesListViewState>()
    private val sonneriesAttenteListeState = MediatorLiveData<MusiquesListViewState>()

    init {
        sonneriesPasseesListeState.addSource(AppWakeUp.getSonneriesPassees()) { newListe ->
            val oldState = sonneriesPasseesListeState.value!!
            sonneriesPasseesListeState.value = oldState.copy(
                hasMusiquesChanged = true,
                musiques = newListe
            )
        }
        sonneriesAttenteListeState.addSource(AppWakeUp.getSonneriesPassees()) { newListe ->
            val oldState = sonneriesAttenteListeState.value!!
            sonneriesAttenteListeState.value = oldState.copy(
                hasMusiquesChanged = true,
                musiques = newListe
            )
        }
    }

    fun getListePasseesLiveData(): LiveData<MusiquesListViewState> = sonneriesPasseesListeState
    fun getListeAttenteLiveData(): LiveData<MusiquesListViewState> = sonneriesAttenteListeState

    fun addSonnerieAttente(idSonnerie: String, sonnerie: SonnerieRecue, context: Context) {
        AppWakeUp.addSonnerieEnAttente(idSonnerie, sonnerie, context)
    }

    fun removeSonnerieEnAttente(idSonnerie: String, musicId: String, context: Context) {
        AppWakeUp.removeSonnerieEnAttente(idSonnerie, musicId, context)
    }

    fun removeSonneriePassee(idSonnerie: String, musicId: String, context: Context) {
        AppWakeUp.removeSonneriePassee(idSonnerie, musicId, context)
    }

}